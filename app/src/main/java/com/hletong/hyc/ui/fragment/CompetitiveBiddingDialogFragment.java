package com.hletong.hyc.ui.fragment;

import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.mob.util.ViewUtil;
import com.xcheng.view.util.LocalDisplay;

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ddq on 2016/11/1.
 */

public class CompetitiveBiddingDialogFragment extends DialogFragment implements View.OnClickListener {
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.content)
    TextView content;
    @BindView(R.id.content_container)
    LinearLayout content_container;
    @BindView(R.id.two_button)
    ViewStub tb;

    private EditText et;
    private MoneyChanged mc;
    private int billingType;

    public static CompetitiveBiddingDialogFragment getInstance(int billingType) {
        Bundle bundle = new Bundle();
        bundle.putInt("billingType", billingType);
        CompetitiveBiddingDialogFragment fragment = new CompetitiveBiddingDialogFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, R.style.dialog_centerTip);
        billingType = getArguments().getInt("billingType");
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_dialog_common, container, false);
        ButterKnife.bind(this, view);
        initButtons();
        content_container.removeView(content);
        View input = getActivity().getLayoutInflater().inflate(R.layout.dialog_cb_input, content_container, false);
        ViewGroup.MarginLayoutParams params = new ViewGroup.MarginLayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, LocalDisplay.dp2px(48));
        params.topMargin = LocalDisplay.dp2px(15);
        content_container.addView(input, params);
        TextView label = (TextView) input.findViewById(R.id.l1);
        if (billingType == 1)
            label.setText("修改合计");
        et = (EditText) input.findViewById(R.id.input);
        if (billingType == 1)
            title.setText(String.format(Locale.CHINA, "您的合计%s", getArguments().getString("bidPrice")));
        else
            title.setText(String.format(Locale.CHINA, "您的竞价%s", getArguments().getString("bidPrice")));
        return view;
    }

    public void setBidPrice(String bidPrice) {
        Bundle bundle = new Bundle();
        bundle.putString("bidPrice", bidPrice);
        setArguments(bundle);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        et.setText(null);
        super.onDismiss(dialog);
    }

    private void initButtons() {
        View buttons = tb.inflate();

        final float radius = getActivity().getResources().getDimension(com.hletong.mob.R.dimen.tipDialog_conner);

        TextView po = (TextView) buttons.findViewById(com.hletong.mob.R.id.id_positiveBtn);
        ViewUtil.setDialogViewBackground(po, ContextCompat.getColor(getActivity(), com.hletong.mob.R.color.dialog_grey_bg), new float[]{0, 0, 0, 0, radius, radius, 0, 0});
        po.setText(R.string.ok);
        po.setOnClickListener(this);

        TextView ne = (TextView) buttons.findViewById(com.hletong.mob.R.id.id_negativeBtn);
        ViewUtil.setDialogViewBackground(ne, ContextCompat.getColor(getActivity(), com.hletong.mob.R.color.dialog_grey_bg), new float[]{0, 0, 0, 0, 0, 0, radius, radius});
        ne.setText(R.string.cancel);
        ne.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.id_positiveBtn:
                if (mc != null)
                    mc.onMoneyChanged(et.getText().toString());
            case R.id.id_negativeBtn:
                dismiss();
                break;
        }
    }

    public void setMc(MoneyChanged mc) {
        this.mc = mc;
    }

    public interface MoneyChanged {
        void onMoneyChanged(String money);
    }
}
