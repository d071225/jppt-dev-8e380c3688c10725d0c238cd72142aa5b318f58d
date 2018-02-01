package com.hletong.hyc.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.model.ETCCard;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.ui.fragment.SelectPlateFragment;
import com.hletong.hyc.util.LoginFilterDelegate;
import com.hletong.mob.base.BaseActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CardDetailActivity extends BaseActivity {
    @BindView(R.id.iv_logo)
    ImageView imageView;
    @BindView(R.id.tv_etc2)
    TextView tv2;
    @BindView(R.id.tv_etc3)
    TextView tv3;

    @Override
    public int getLayoutId() {
        return R.layout.activity_card_detail;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        setCustomTitle(R.string.hl_etc_card);
        int position = getIntent().getIntExtra("position", 0);
        if (position == 1) {
            imageView.setImageResource(R.drawable.hl_credit_card_large);
            tv2.setText(tv2.getText().toString().replace("ETC", ""));
            tv3.setText(tv3.getText().toString().replace("ETC", ""));
        }
    }

    @OnClick({R.id.apply_now})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_now:
                if (!willFilter(v)) {
                    Bundle bundle = new Bundle();
                    ETCCard etcCard = getParcelable("etc");
                    bundle.putString("CardType", etcCard.getCardId());
                    toActivity(
                            CommonWrapFragmentActivity.class,
                            CommonWrapFragmentActivity.getBundle("选择车牌号", SelectPlateFragment.class, bundle),
                            null);
                }
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        filterResume();
    }

    private LoginFilterDelegate mLoginFilterDelegate;

    private void filterResume() {
        if (LoginInfo.getLoginInfo() == null && mLoginFilterDelegate == null) {
            mLoginFilterDelegate = new LoginFilterDelegate(this);
        }
        if (mLoginFilterDelegate != null) {
            mLoginFilterDelegate.handleResume();
        }
    }

    private boolean willFilter(View view) {
        if (getString(R.string.hl_tag_login).equals(view.getTag())) {
            return mLoginFilterDelegate != null && mLoginFilterDelegate.willFilter(view);
        }
        return false;
    }
}
