package com.hletong.hyc.ui.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.SelectPlateAdapter;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.VehicleInfo;
import com.hletong.hyc.ui.activity.ETCIntroducePaperActivity;
import com.hletong.hyc.ui.activity.VehicleInfoActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.architect.error.BusiError;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.DividerItemDecoration;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by ddq on 2017/2/3.
 */

public class SelectPlateFragment extends BaseRefreshFragment<VehicleInfo> implements SelectPlateAdapter.OnPlateSelected {

    @BindView(R.id.btn_next)
    Button btn_next;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!LoginInfo.isChildAccount()) {//主账号才能添加车辆，子账号不行
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.addcar, menu);
        MenuItem mi = menu.findItem(R.id.addCar);
        mi.setTitle(R.string.add_car);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Bundle bundle = new Bundle();
        bundle.putString("CardType", getArguments().getString("CardType"));
        toActivity(VehicleInfoActivity.class, bundle, null);
        return true;
    }

    @Override
    public int getLayoutId() {
        return R.layout.fragment_select_plate;
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.VEHICLE_INFO);
    }


    @NonNull
    @Override
    public HFRecyclerAdapter<VehicleInfo> createAdapter() {
        return new SelectPlateAdapter(getActivity(), null, this);
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new DividerItemDecoration();
    }

    @Override
    public void plateSelected(VehicleInfo vehicleInfo) {
        btn_next.setEnabled(true);
        btn_next.setTag(vehicleInfo);
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        super.handleError(error);
        if (error instanceof BusiError) {
            if (((BusiError) error).getBusiCode() == -1) {
                DialogFactory.showAlert(getContext(), error.getMessage(), null, false, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
            }
        }
    }

    @OnClick({R.id.btn_next, R.id.tv_introduce})
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_next) {
            Bundle bundle = new Bundle();
            bundle.putString("CardType", getArguments().getString("CardType"));
            bundle.putParcelable("vehicleInfo", (VehicleInfo) btn_next.getTag());
            toActivity(VehicleInfoActivity.class, bundle, null);
        } else if (v.getId() == R.id.tv_introduce) {
            toActivity(ETCIntroducePaperActivity.class, null, null);
        }
    }

    @Override
    protected String getEntry() {
        return "data";
    }

    @Override
    public void showList(List<VehicleInfo> mList, boolean refresh) {
        Iterator<VehicleInfo> iterator = mList.iterator();
        while (iterator.hasNext()) {
            VehicleInfo vehicleInfo = iterator.next();
            if (TextUtils.isEmpty(vehicleInfo.getPlate())) {
                iterator.remove();
            }
        }
        super.showList(mList, refresh);
    }
}
