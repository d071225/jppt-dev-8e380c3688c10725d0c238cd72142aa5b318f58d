package com.hletong.hyc.util;

import com.hletong.hyc.R;
import com.hletong.hyc.model.LoginInfo;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.RegisterPhoto;
import com.xcheng.view.EasyView;

import java.util.ArrayList;

/**
 * Created by chengxin on 2017/7/5.
 */
public class PapersHelper {
    public static ArrayList<PaperPhoto> getTruckPersonalChild() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.upload_front_photo), EasyView.getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "车辆所有人身份证", 13));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.driving_license_info)}, new int[]{R.drawable.src_driver_license}, 0.75f), "车辆行驶证", 14));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传道路运输经营许可证"}, 0.75f), "道路运输经营许可证", 5));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传道路运输从业资格证"}, 0.75f), "道路运输从业资格证", 33));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传车辆营运证"}, 0.75f), "车辆营运证", 15));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传挂靠协议照片"}, 0.62f), "挂靠协议", 20));
        return paperPhotos;
    }

    public static ArrayList<PaperPhoto> getShipManager() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();
        if (LoginInfo.isCompany()) {
            paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.upload_front_photo), EasyView.getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "船舶负责人身份证", 31));
        } else {
            paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.upload_front_photo), EasyView.getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "船舶所有人身份证", 23));
        }
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶营运证"}, 0.62f), "船舶营运证", 26));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传水路运输经营许可证"}, 0.62f), "水路运输经营许可", 7));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶适航证书"}, 0.62f), "船舶适航证书", 27));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶国籍证书封面照片", "请上传船舶国籍证书第一页照片"}, new int[]{R.drawable.src_ship_license_cover, R.drawable.src_ship_license_first}, 1.33f), "船舶国籍证书", 25));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶所有权证"}, 0.62f), "船舶所有权证", 29));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传船舶检验证书"}, 0.62f), "船舶检验证书", 28));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传挂靠协议照片"}, 0.62f), "挂靠协议", 20));

        return paperPhotos;
    }

    public static ArrayList<PaperPhoto> getTruckManager() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();
        if (LoginInfo.isCompany()) {
            paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.upload_front_photo), EasyView.getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "车辆负责人身份证", 16));
        } else {
            paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.upload_front_photo), EasyView.getString(R.string.upload_contrary_photo)}, new int[]{R.drawable.src_identify_front, R.drawable.src_identify_back}, 0.62f), "车辆所有人身份证", 13));
        }
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{EasyView.getString(R.string.driving_license_info)}, new int[]{R.drawable.src_driver_license}, 0.75f), "车辆行驶证", 14));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传车辆营运证"}, 0.75f), "车辆营运证", 15));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传道路运输经营许可证"}, new int[1], 0.75f), "道路运输经营许可证", 5));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传道路运输从业资格证"}, new int[1], 0.75f), "道路运输从业资格证", 33));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传挂靠协议照片"}, 0.62f), "挂靠协议", 20));
        return paperPhotos;
    }

    public static ArrayList<PaperPhoto> getCompanyMainSingleChoice() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f), "营业执照(三证合一)", 1));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传组织机构代码"}, 0.62f), "组织机构代码", 2));
        return paperPhotos;
    }

    public static ArrayList<PaperPhoto> getTruckCompanyMain() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传旧版营业执照照片"}, new int[]{R.drawable.src_busi_license_old}, 0.87f), "旧版营业执照", 2));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f), "三证合一营业执照", 6));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传组织机构代码"}, 0.62f), "组织机构代码", 3));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传税务登记证"}, 0.62f), "税务登记证", 4));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传道路运输经营许可证"}, 0.62f), "道路运输经营许可证", 5));
        return paperPhotos;
    }

    public static ArrayList<PaperPhoto> getCompanyShip() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();

        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传旧版营业执照照片"}, new int[]{R.drawable.src_busi_license_old}, 0.87f), "营业执照", 2));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传组织机构代码照片"}, 0.62f), "组织机构代码", 3));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传税务登记证照片"}, 0.87f), "税务登记证", 4));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f), "营业执照(三证合一)", 6));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传水路运输经营许可证"}, 0.62f), "水路运输经营许可", 7));


        return paperPhotos;
    }

    public static ArrayList<PaperPhoto> getCompanyCargo() {
        ArrayList<PaperPhoto> paperPhotos = new ArrayList<>();
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传三证合一营业执照照片"}, new int[]{R.drawable.src_busi_license_new}, 1.33f), "营业执照(三证合一)", 6));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传旧版营业执照照片"}, new int[]{R.drawable.src_busi_license_old}, 0.87f), "营业执照", 2));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传税务登记证照片"}, 0.87f), "税务登记证", 4));
        paperPhotos.add(new PaperPhoto(new RegisterPhoto(new String[]{"请上传组织机构代码照片"}, 0.62f), "组织机构代码", 3));
        return paperPhotos;
    }
}
