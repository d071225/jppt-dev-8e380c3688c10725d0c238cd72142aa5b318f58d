package com.hletong.hyc.ui.activity.ship;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.ShipInfo;
import com.hletong.hyc.ui.activity.BankCardAddActivity;
import com.hletong.hyc.ui.activity.PapersAddActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.PapersHelper;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 船舶个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class ShipCompanyChildCertShipManagerActivity extends BaseActivity {
    @BindView(R.id.cv_ship)
    CommonInputView cv_ship;

    @BindView(R.id.cv_shipInfos)
    CommonInputView cv_shipInfos;


    @BindView(R.id.cv_papersInfo)
    CommonInputView cv_papersInfo;

    //证件资料
    @BindView(R.id.cv_bankcardInfo)
    CommonInputView cv_bankcardInfo;
    static final int SHIPINFO = 10;


    private ShipInfo mShipInfo;

    private ArrayList<PaperPhoto> mPaperPhotos;

    private ArrayList<BankCard> mBankCards;


    private boolean validate() {
        String ship = cv_ship.getInputValue();
        if (StringUtil.isTrimBlank(ship)) {
            showMessage("船号不能为空");
            return false;
        } else if (mShipInfo == null) {
            showMessage("请选择船舶信息");
            return false;
        } else if (mPaperPhotos == null) {
            showMessage("请选择证件信息");
            return false;
        } else if (mBankCards == null) {
            showMessage("请选择银行卡信息");
            return false;
        }
        return true;
    }

    private HashMap<String, Object> mShipManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_ship_company_cert_child_shipmanager;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        if (getBundle() != null) {
            //自主认证船号不能编辑
            cv_ship.getInput().setEnabled(!getBundle().getBoolean("isOwnCert"));
        }
        mShipManager = getSerializable("shipManager");
        if (mShipManager != null) {
            cv_ship.setText((String) mShipManager.get("ship"));
            mShipInfo = (ShipInfo) mShipManager.get("shipInfo");
            if (mShipInfo != null) {
                cv_shipInfos.setText("已填写");
            }
            mPaperPhotos = (ArrayList<PaperPhoto>) mShipManager.get("papersInfo");

            if (mPaperPhotos != null) {
                for (PaperPhoto photo : mPaperPhotos) {
                    if (photo.canShow()) {
                        cv_papersInfo.setText("已填写");
                        break;
                    }
                }
            }
            mBankCards = (ArrayList<BankCard>) mShipManager.get("bankcards");
            if (mBankCards != null) {
                cv_bankcardInfo.setText("已填写");
            }
        } else {
            mShipManager = new HashMap<>();
        }
    }

    @OnClick({R.id.cv_bankcardInfo, R.id.cv_shipInfos, R.id.cv_papersInfo, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_bankcardInfo) {
            toActivity(BankCardAddActivity.class, JumpUtil.getBundle(BankCard.class.getSimpleName(), mBankCards), Constant.BANKCARD_REQUEST, null);
        } else if (v.getId() == R.id.cv_shipInfos) {
            Bundle bundle = JumpUtil.getBundle(ShipInfo.class.getSimpleName(), mShipInfo);
            toActivity(ShipInfoAddActivity.class, bundle, SHIPINFO, null);
        } else if (v.getId() == R.id.cv_papersInfo) {
            if (mPaperPhotos == null) {
                mPaperPhotos = PapersHelper.getShipManager();
            }
            toActivity(PapersAddActivity.class, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mPaperPhotos), Constant.PAPER_REQUEST, null);
        } else if (v.getId() == R.id.btn_commit) {
            if (validate()) {
                mShipManager.put("ship", cv_ship.getInputValue());
                mShipManager.put("shipInfo", mShipInfo);
                mShipManager.put("papersInfo", mPaperPhotos);
                mShipManager.put("bankcards", mBankCards);
                Intent intent = new Intent();
                intent.putExtra("shipManager", mShipManager);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            CallPhoneDialog.getInstance().show(getSupportFragmentManager());
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == SHIPINFO) {
            mShipInfo = (ShipInfo) data.getSerializableExtra(ShipInfo.class.getSimpleName());
            cv_shipInfos.setText("已填写");
        } else if (requestCode == Constant.PAPER_REQUEST) {
            mPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
            cv_papersInfo.setText("已填写");
        } else if (requestCode == Constant.BANKCARD_REQUEST) {
            mBankCards = (ArrayList<BankCard>) data.getSerializableExtra(BankCard.class.getSimpleName());
            cv_bankcardInfo.setText("已填写");
        }
    }
}
