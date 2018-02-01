package com.hletong.hyc.ui.fragment;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.contract.FavoriteRoutineContract;
import com.hletong.hyc.model.Address;
import com.hletong.hyc.model.FavoriteRoutine;
import com.hletong.hyc.presenter.FavoriteRoutinePresenter;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.SimpleProgress;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.util.ParamsHelper;

/**
 * Created by ddq on 2016/12/30.
 * 常跑路线查询到的货源
 */

public class SourceListFragmentFavorite extends BaseSourceListFragment implements FavoriteRoutineContract.View {
    private FavoriteRoutineContract.Presenter mPresenter;

    public static Bundle getBundle(FavoriteRoutine favoriteRoutine) {
        Bundle bundle = new Bundle();
        Address.setAddress(bundle, new Address[]{favoriteRoutine.getStartAddress(), favoriteRoutine.getEndAddress()});
        bundle.putString("routeUuid", favoriteRoutine.getRouteUuid());
        return bundle;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new FavoriteRoutinePresenter(this, Address.getAddressByIndex(getArguments(), 0), Address.getAddressByIndex(getArguments(), 1));
        if (mPresenter.isCargoParamValid())
            setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.history, menu);
        MenuItem menuItem = menu.getItem(0);
        menuItem.setTitle("删除线路");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        mPresenter.delete(getArguments().getString("routeUuid"), new SimpleProgress(getContext()));
        return true;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViewById(R.id.selector).setVisibility(View.GONE);
    }

    @Override
    protected ParamsHelper getRequestJson(boolean refresh){
        ParamsHelper jsonObject = new ParamsHelper();
        jsonObject
                .put("start", refresh ? 1 : mAdapter.getDataCount() + 1)
                .put("limit", mAdapter.getLength())
                .put("loadingProvince", mPresenter.getStartAddress().getProvinceForQuery())
                .put("loadingCity", mPresenter.getStartAddress().getCityForQuery())
                .put("loadingCountry", mPresenter.getStartAddress().getCountyForQuery())
                .put("unloadProvince", mPresenter.getEndAddress().getProvinceForQuery())
                .put("unloadCity", mPresenter.getEndAddress().getCityForQuery())
                .put("unloadCountry", mPresenter.getEndAddress().getCountyForQuery())
                .put("orginCargonKindName", "")
                .put("dateTypeEnum", 0)
                .put("transportType", BuildConfig.carrier_type);
        return jsonObject;
    }

    @Override
    protected boolean isAutoLoad() {
        return mPresenter.isCargoParamValid();
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.GOODS_NOTICE);
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_source_list;
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(R.drawable.icon_empty_source_list);
    }
}
