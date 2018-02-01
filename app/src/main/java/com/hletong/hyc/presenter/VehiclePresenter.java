package com.hletong.hyc.presenter;

import android.support.annotation.NonNull;
import android.support.v4.util.SimpleArrayMap;

import com.hletong.hyc.contract.VehicleInfoContract;
import com.hletong.hyc.model.DictionaryItem;
import com.hletong.hyc.model.EtcApplyInfo;
import com.hletong.hyc.model.validate.EtcTransporterInfoPart2;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.architect.model.repository.SimpleCallback;
import com.hletong.mob.architect.presenter.BasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.util.IdentityNoParseUtil;
import com.hletong.mob.util.pinyin.PinyinFormat;
import com.hletong.mob.util.pinyin.PinyinHelper;

import java.util.ArrayList;

public class VehiclePresenter extends BasePresenter<VehicleInfoContract.View> implements VehicleInfoContract.Presenter {
    private EtcApplyInfo mInfoPart1;//这是上一个Activity（VehicleInfoActivity）传入的数据
    private SimpleArrayMap<Integer, SelectorDataWrapper> itemData;


    public VehiclePresenter(VehicleInfoContract.View vehicleInfoView, EtcApplyInfo mInfoPart) {
        super(vehicleInfoView);
        this.mInfoPart1 = mInfoPart;
    }

    @Override
    public void start() {
        itemData = new SimpleArrayMap<>(3);

        ArrayList<DictionaryItem> id = new ArrayList<>();
        id.add(new DictionaryItem("1", "身份证"));
        itemData.put(VehicleInfoContract.TYPE_IDENTITY, new SelectorDataWrapper(id, "证件类型", "selectDocument"));

        ArrayList<DictionaryItem> validity = new ArrayList<>();
        validity.add(new DictionaryItem("0", "非长期有效"));
        validity.add(new DictionaryItem("1", "长期有效"));
        itemData.put(VehicleInfoContract.TYPE_VALIDITY, new SelectorDataWrapper(validity, "证件有效期", "idValid"));

        ArrayList<DictionaryItem> sex = new ArrayList<>();
        sex.add(new DictionaryItem("0", "男"));
        sex.add(new DictionaryItem("1", "女"));
        itemData.put(VehicleInfoContract.TYPE_SEX, new SelectorDataWrapper(sex, "性别", "selectSex"));

        //UI初始化
        getView().initBasic(mInfoPart1.getNameForDisplay(), mInfoPart1.getPhone(), mInfoPart1.getHletCardType(), mInfoPart1.getPlate());
        getView().initValidity(validity.get(mInfoPart1.getDocumentValid()), mInfoPart1.getParsedDVD());//证件有效期
        getView().initIdentityNo(id.get(0), mInfoPart1.getDocumentCode());//证件信息
       // nameChanged(mInfoPart1.getNameForDisplay());//初始化姓名拼音
        identityChanged(mInfoPart1.getDocumentCode());//个人信息
    }

    @Override
    public void identityChanged(String identity) {
        IdentityNoParseUtil.Result result = IdentityNoParseUtil.parseIdentityNo(identity);
        getView().initPersonalInfo(itemData.get(VehicleInfoContract.TYPE_SEX).data.get(result.getSex()), result.getBirthday());
    }

    @Override
    public void nameChanged(String name) {
        final String s = PinyinHelper.convertToPinyinString(name, " ", PinyinFormat.WITHOUT_TONE);
        if (s != null)
            getView().initNameSpell(s.toUpperCase());
        else
            getView().initNameSpell(null);
    }

    @Override
    public void submit(EtcTransporterInfoPart2 mInfoPart2) {
        if (!mInfoPart2.validate(getView())) {
            return;
        }

        mInfoPart1.set(mInfoPart2);

        ItemRequestValue<String> requestValue = new ItemRequestValue<String>(
                getView().hashCode(),
                Constant.getUrl(Constant.ETC_SUBMIT_INFO),
                mInfoPart1.getParams(),
                "data"){};

        getDataRepository().loadItem(requestValue, new SimpleCallback<String>(getView()) {
            @Override
            public void onSuccess(@NonNull String response) {
                getView().onSuccess(response);
            }
        });
    }

    @Override
    public void prepareDataForSelector(@VehicleInfoContract.SelectorType int type, int extra) {
        SelectorDataWrapper dataWrapper = itemData.get(type);
        getView().showSelector(dataWrapper.data, dataWrapper.type, dataWrapper.title, extra);
    }

    private class SelectorDataWrapper {
        ArrayList<DictionaryItem> data;
        String title;
        String type;

        public SelectorDataWrapper(ArrayList<DictionaryItem> data, String title, String type) {
            this.data = data;
            this.title = title;
            this.type = type;
        }
    }
}
