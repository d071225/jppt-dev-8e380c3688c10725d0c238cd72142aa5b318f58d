package com.hletong.hyc.ui.fragment;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.AppContract;
import com.hletong.hyc.model.UnTodo;
import com.hletong.hyc.ui.activity.AppGuideHelper;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.activity.MessageActivity;
import com.hletong.hyc.ui.activity.test.CargoMatchActivity;
import com.hletong.mob.widget.UndoTextView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 首页各模块入口
 * Created by cc on 2016/10/13.
 */
public class AppCargoFragment extends AppFragment implements AppContract.View {
    //和车船网签承运一样
    @BindView(R.id.hfht)
    UndoTextView hfht;
    @BindView(R.id.fhd)
    UndoTextView fhd;
    @BindView(R.id.shd)
    UndoTextView shd;
    @BindView(R.id.xx)
    UndoTextView xx;

    @Override
    public int getLayoutId() {
        return R.layout.fragment_app_hyh;
    }

    @OnClick({R.id.hyyb, R.id.hfht, R.id.fhd, R.id.shd, R.id.xx, R.id.zf})
    @Override
    public void onClick(android.view.View v) {
       /* if (willFilter(v)) {
            return;
        }*/
        switch (v.getId()) {
            case R.id.hyyb:
                toActivity(CargoMatchActivity.class, null, null);
//                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.cargo_forecast), CargoSourceListFragment.class), null);
                break;
            case R.id.hfht:
                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.contract_hfht), ContractUnSignedFragment.class), null);
                break;
            case R.id.fhd:
                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.invoice_block), InvoiceFragment.class), null);
                break;
            case R.id.shd:
                toActivity(CommonWrapFragmentActivity.class, CommonWrapFragmentActivity.getBundle(getString(R.string.receipt_block), ReceiptFragment.class), null);
                break;
            case R.id.xx:
                toActivity(MessageActivity.class, null, null);
                break;
            case R.id.zf:
                showMessage("敬请期待");
                break;
        }
    }

    @Override
    public void setUnToDoInfo(UnTodo unTodo) {
        hfht.setUndoMessage(unTodo.getHuofanghetong());
        fhd.setUndoMessage(unTodo.getFahuodanhuichuang());
        shd.setUndoMessage(unTodo.getShouhuodanhuichuan());
        xx.setUndoMessage(unTodo.getMessageCount());
    }

    @Override
    protected void autoAdFirstOpen() {

    }

    @Override
    protected void appGuideFirstOpen(AppGuideHelper guideHelper) {
        guideHelper.showCargoTipView();
    }
}
