package com.hletong.hyc.ui.activity.truck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.model.BankCard;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.TruckCompleteInfo;
import com.hletong.hyc.ui.activity.BankCardAddActivity;
import com.hletong.hyc.ui.activity.PapersAddActivity;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.hyc.util.Constant;
import com.hletong.hyc.util.PapersHelper;
import com.hletong.hyc.util.RegexUtil;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.util.StringUtil;
import com.xcheng.view.util.JumpUtil;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class TruckCompanyChildCertTruckManagerActivity extends BaseActivity {
    @BindView(R.id.cv_plate)
    CommonInputView cv_plate;

    @BindView(R.id.cv_truckInfos)
    CommonInputView cv_truckInfos;


    @BindView(R.id.cv_papersInfo)
    CommonInputView cv_papersInfo;

    //证件资料
    @BindView(R.id.cv_bankcardInfo)
    CommonInputView cv_bankcardInfo;
    static final int TRUCKINFO = 10;


    private TruckCompleteInfo mTruckInfo;

    private ArrayList<PaperPhoto> mPaperPhotos;

    private ArrayList<BankCard> mBankCards;


    private boolean validate() {
        String plate = cv_plate.getInputValue();
        if (StringUtil.isTrimBlank(plate)) {
            showMessage("车牌号不能为空");
            return false;
        } else if (RegexUtil.containEmpty(plate)) {
            showMessage("车牌号不能包含空格");
            return false;
        } else if (!RegexUtil.isCarNumber(plate)) {
            showMessage("车牌号格式不正确");
            return false;
        } else if (mTruckInfo == null) {
            showMessage("请选择车辆信息");
            return false;
        }
//        else if (mPaperPhotos == null) {
//            showMessage("请选择证件信息");
//            return false;
//        } else if (mBankCards == null) {
//            showMessage("请选择银行卡信息");
//            return false;
//        }
        return true;
    }

    private HashMap<String, Object> mTruckManager;

    @Override
    public int getLayoutId() {
        return R.layout.activity_truck_company_cert_child_truckmanager;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        if (getBundle() != null) {
            //自主认证车号不能编辑
            cv_plate.getInput().setEnabled(!getBundle().getBoolean("isOwnCert"));
        }

        mTruckManager = getSerializable(TruckCompanyChildCertActivity.KEY_TRUCK_MANAGER);
        if (mTruckManager != null) {
            cv_plate.setText((String) mTruckManager.get("plate"));
            mTruckInfo = (TruckCompleteInfo) mTruckManager.get("truckInfo");
            if (mTruckInfo != null) {
                cv_truckInfos.setText("已填写");
            }
            mPaperPhotos = (ArrayList<PaperPhoto>) mTruckManager.get("papersInfo");
            if (mPaperPhotos != null) {
                for (PaperPhoto photo : mPaperPhotos) {
                    if (photo.canShow()) {
                        cv_papersInfo.setText("已填写");
                        break;
                    }
                }
            }
            mBankCards = (ArrayList<BankCard>) mTruckManager.get("bankcards");
            if (mBankCards != null) {
                cv_bankcardInfo.setText("已填写");
            }
        } else {
            mTruckManager = new HashMap<>();
        }
    }

    @OnClick({R.id.cv_bankcardInfo, R.id.cv_truckInfos, R.id.cv_papersInfo, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (v.getId() == R.id.cv_bankcardInfo) {
            toActivity(BankCardAddActivity.class, JumpUtil.getBundle(BankCard.class.getSimpleName(), mBankCards), Constant.BANKCARD_REQUEST, null);
        } else if (v.getId() == R.id.cv_truckInfos) {
            Bundle bundle = JumpUtil.getBundle(TruckCompleteInfo.class.getSimpleName(), mTruckInfo);
            toActivity(TruckInfoAddActivity.class, bundle, TRUCKINFO, null);
        } else if (v.getId() == R.id.cv_papersInfo) {
            if (mPaperPhotos == null) {
                mPaperPhotos = PapersHelper.getTruckManager();
            }
            toActivity(PapersAddActivity.class, JumpUtil.getBundle(PaperPhoto.class.getSimpleName(), mPaperPhotos), Constant.PAPER_REQUEST, null);
        } else if (v.getId() == R.id.btn_commit) {
            if (validate()) {
                mTruckManager.put("plate", cv_plate.getInputValue());
                mTruckManager.put("truckInfo", mTruckInfo);
                mTruckManager.put("papersInfo", mPaperPhotos);
                mTruckManager.put("bankcards", mBankCards);
                Intent intent = new Intent();
                intent.putExtra(TruckCompanyChildCertActivity.KEY_TRUCK_MANAGER, mTruckManager);
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
        if (requestCode == TRUCKINFO) {
            mTruckInfo = (TruckCompleteInfo) data.getSerializableExtra(TruckCompleteInfo.class.getSimpleName());
            cv_truckInfos.setText("已填写");
        } else if (requestCode == Constant.PAPER_REQUEST) {
            mPaperPhotos = (ArrayList<PaperPhoto>) data.getSerializableExtra(PaperPhoto.class.getSimpleName());
            cv_papersInfo.setText("已填写");
        } else if (requestCode == Constant.BANKCARD_REQUEST) {
            mBankCards = (ArrayList<BankCard>) data.getSerializableExtra(BankCard.class.getSimpleName());
            cv_bankcardInfo.setText("已填写");
        }
    }
}
