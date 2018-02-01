package com.hletong.hyc.model;

import android.support.annotation.NonNull;
import android.view.View;

import com.hletong.mob.dialog.selector.IItemShow;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by chengxin on 2017/6/19.
 */

public class PaperPhoto implements IItemShow, Serializable {
    //单选
    public static final String KET_SINGLE = "key_single";
    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);
    private static final long serialVersionUID = -6034104474175581893L;

    private RegisterPhoto registerPhoto;
    private String value;
    private String input;
    private String date;
    private int paperType;
    private String paperUuid;
    //列表中的位置
    private boolean inList;
    /**
     * 对应View的Id
     */
    private int generateId;

    public PaperPhoto(@NonNull RegisterPhoto registerPhoto, @NonNull String value, int paperType) {
        this.registerPhoto = registerPhoto;
        this.value = value;
        this.paperType = paperType;
        this.generateId = 0;
    }

    public void setRegisterPhoto(RegisterPhoto registerPhoto) {
        this.registerPhoto = registerPhoto;
    }

    public RegisterPhoto getRegisterPhoto() {
        return registerPhoto;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof PaperPhoto)) {
            return false;
        }
        PaperPhoto p = (PaperPhoto) obj;
        return p.paperType == paperType;
    }

    /**
     * 数据是否完整
     *
     * @return
     */
    public boolean isDataComplete() {
        return date != null && registerPhoto.canUpload();
    }

    public boolean canShow() {
        return date != null || registerPhoto.getFileGroupId() != null;

    }

    public String getEmptyDataMsg() {
        if (!registerPhoto.canUpload()) {
            return "请上传" + value + "照片";
        } else if (date == null) {
            return "请选择" + value + "到期日期";
        }
        return null;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String getValue() {
        return value;
    }

    public int getPaperType() {
        return paperType;
    }

    public void setPaperType(int paperType) {
        this.paperType = paperType;
    }


    public void reset() {
        generateId = 0;
        inList = false;
        if (registerPhoto != null) {
            registerPhoto.reset();
        }
        date = null;
    }

    public boolean hasGenerateId() {
        return generateId != 0;
    }

    public void setGenerateId() {
        this.generateId = generateViewId();
    }

    public int getGenerateId() {
        return generateId;
    }

    /**
     * Generate a value suitable for use in {@link View#setId(int)}.
     * This value will not collide with ID values generated at build time by aapt for R.id.
     *
     * @return a generated ID value
     */
    public static int generateViewId() {
        for (; ; ) {
            final int result = sNextGeneratedId.get();
            // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
            int newValue = result + 1;
            if (newValue > 0x00FFFFFF) newValue = 1; // Roll over to 1, not 0.
            if (sNextGeneratedId.compareAndSet(result, newValue)) {
                return result;
            }
        }
    }


    public boolean isInList() {
        return inList;
    }

    public void setInList(boolean inList) {
        this.inList = inList;
    }

    public String getPaperUuid() {
        return paperUuid;
    }

    public void setPaperUuid(String paperUuid) {
        this.paperUuid = paperUuid;
    }
}
