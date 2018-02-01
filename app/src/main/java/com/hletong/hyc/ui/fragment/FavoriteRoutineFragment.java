package com.hletong.hyc.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.hyc.adapter.FavoriteRoutineAdapter;
import com.hletong.hyc.contract.FavoriteRoutineContract;
import com.hletong.hyc.model.FavoriteRoutine;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.ui.activity.AddFavoriteRoutineActivity;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.EmptyView;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;
import com.hletong.mob.util.ParamsHelper;
import com.xcheng.view.enums.RequestState;
import com.xcheng.view.util.LocalDisplay;

/**
 * Created by ddq on 2017/2/10.
 * 常用路线列表
 */

public class FavoriteRoutineFragment extends BaseRefreshFragment<FavoriteRoutine> implements FavoriteRoutineContract.View {
    private String memberCode;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        memberCode = LoginInfo.getLoginInfo().getMember_code();
    }

    @Override
    protected String getRequestUrl() {
        return Constant.getUrl(Constant.FAVORITE_ROUTINE);
//        return "http://192.168.2.28:8080/jppt/action/public/vehicles/route/get";//罗星测试环境
    }


    @Override
    protected ParamsHelper getRequestJson(boolean refresh) {
        return super.getRequestJson(refresh).put("memberCode", memberCode);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<FavoriteRoutine> createAdapter() {
        return new FavoriteRoutineAdapter(getContext(), this, this);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        LinearLayout mLinearLayout = (LinearLayout) getActivity().findViewById(R.id.layout_root);
        TextView tv = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.common_button, null);
        tv.setText(R.string.add_favorite_routine);
        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toActivity(AddFavoriteRoutineActivity.class, null, 100, null);
            }
        });
        ViewGroup.LayoutParams mLayoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(48));
        mLinearLayout.addView(tv, mLayoutParams);
    }

    @Override
    protected String getEntry() {
        return null;
    }

    @Override
    public EmptyView getEmptyView() {
        return super.getEmptyView().setEmptyLogo(BuildConfig.transporter_empty_view).setCustomState(RequestState.NO_DATA, "暂无常跑路线");
    }

    @Override
    public void onActivityResultOk(int requestCode, Intent data) {
        super.onActivityResultOk(requestCode, data);
        //添加常跑路线成功或者是删除常跑路线成功
        if (data != null && data.getStringExtra("memberCode") != null)
            memberCode = data.getStringExtra("memberCode");
        requestData(true);
    }

    @Override
    public void success(String message) {
        //这里是长按列表项删除常跑路线的返回结果，删除步骤在FavoriteRoutineAdapter里面
        showMessage(message);
        requestData(true);
    }
}
