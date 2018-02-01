package com.hletong.mob.validator.result;

import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.hletong.mob.validator.Validator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 所有表当Result最终构建成需要的结果
 * resultType：保存表单数据的类型
 * 如果是字符类型：resultText ,resultType：TEXT NUMBER DECIMAL 三种情况
 * 如果是列表 basicList,resultType: LIST
 * 如果是对象 basicMap ,resultType: MAP
 * Created by chengxin on 2017/8/3.
 */
public final class ResultBasic implements Parcelable {

    @ResultType
    private final int resultType;

    /**
     * UI展示的内容
     */
    private final String displayText;

    private String resultText;

    private List<ResultBasic> basicList;

    private Map<String, ResultBasic> basicMap;

    /**
     * 基础构造方法
     *
     * @param resultType  内容的类型
     * @param displayText 内容的显示字符
     */
    private ResultBasic(@ResultType int resultType, String displayText) {
        this.resultType = resultType;
        this.displayText = ensureNonNull(displayText);
        resultText = Validator.EMPTY;
    }

    @SuppressWarnings({"WrongConstant"})
    protected ResultBasic(Parcel in) {
        resultType = in.readInt();
        displayText = in.readString();
        resultText = in.readString();
        if (resultType == ResultType.MAP) {
            Bundle bundle = in.readBundle(ResultBasic.class.getClassLoader());
            basicMap = new HashMap<>();
            for (String key : bundle.keySet()) {
                basicMap.put(key, ResultBasic.class.cast(bundle.getParcelable(key)));
            }
        } else if (resultType == ResultType.LIST) {
            basicList = in.createTypedArrayList(ResultBasic.CREATOR);
        }
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(resultType);
        dest.writeString(displayText);
        dest.writeString(resultText);
        if (resultType == ResultType.MAP) {
            Bundle bundle = new Bundle();
            for (Map.Entry<String, ResultBasic> entry : basicMap.entrySet()) {
                bundle.putParcelable(entry.getKey(), entry.getValue());
            }
            dest.writeBundle(bundle);
        } else if (resultType == ResultType.LIST) {
            dest.writeTypedList(basicList);
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ResultBasic> CREATOR = new Creator<ResultBasic>() {
        @Override
        public ResultBasic createFromParcel(Parcel in) {
            return new ResultBasic(in);
        }

        @Override
        public ResultBasic[] newArray(int size) {
            return new ResultBasic[size];
        }
    };

    public static ResultBasic textOf(@ResultType int resultType, @Nullable String resultText, String displayText) {
        if (!isSimpleType(resultType)) {
            throw new IllegalStateException("resultType must be TEXT , NUMBER or DECIMAL");
        }
        ResultBasic resultBasic = new ResultBasic(resultType, displayText);
        resultBasic.resultText = ensureNonNull(resultText);
        return resultBasic;
    }

    /**
     * 输入性质 displayText 赋值为resultText
     */
    public static ResultBasic textOf(@ResultType int resultType, @Nullable String resultText) {
        return ResultBasic.textOf(resultType, resultText, resultText);
    }

    /**
     * resultType 为 TEXT, displayText为 resultText
     */
    public static ResultBasic textOf(@Nullable String resultText) {
        return ResultBasic.textOf(ResultType.TEXT, resultText);
    }

    public static ResultBasic mapOf(@Nullable Map<String, ResultBasic> map, String displayText) {
        ResultBasic resultBasic = new ResultBasic(ResultType.MAP, displayText);
        resultBasic.basicMap = new HashMap<>();
        if (map != null) {
            resultBasic.basicMap.putAll(map);
        }
        return resultBasic;
    }

    public static ResultBasic listOf(@Nullable List<ResultBasic> list, String displayText) {
        ResultBasic resultBasic = new ResultBasic(ResultType.LIST, displayText);
        resultBasic.basicList = new ArrayList<>();
        if (list != null) {
            resultBasic.basicList.addAll(list);
        }
        return resultBasic;
    }

    @NonNull
    private static String ensureNonNull(String text) {
        return text != null ? text : Validator.EMPTY;
    }


    /**
     * 是否为简单的结果类型,
     * resultType为{@link ResultType#TEXT} 或 {@link ResultType#NUMBER} 或 {@link ResultType#DECIMAL}
     *
     * @param resultType 结果类型
     * @return 是否为简单的数据类型
     */
    public static boolean isSimpleType(int resultType) {
        return resultType == ResultType.TEXT
                || resultType == ResultType.NUMBER
                || resultType == ResultType.DECIMAL;
    }


    /**
     * @param resultType 是否为ResultBasic对应的类型
     * @return 如果类型正确返回true
     */
    public boolean checkResultType(int resultType) {
        return this.resultType == resultType;
    }

    /**
     * 检测类型是否正确，否抛出异常
     *
     * @param resultType
     * @param message
     */
    private void checkIllegalState(int resultType, String message) {
        if (!checkResultType(resultType)) {
            throw new IllegalStateException(message);
        }
    }

    public void put(String key, ResultBasic resultBasic) {
        checkIllegalState(ResultType.MAP, "resultType must be MAP");
        basicMap.put(key, resultBasic);
    }

    public ResultBasic get(String key) {
        checkIllegalState(ResultType.MAP, "resultType must be MAP");
        return basicMap.get(key);
    }

    public ResultBasic remove(String key) {
        checkIllegalState(ResultType.MAP, "resultType must be MAP");
        return basicMap.remove(key);
    }

    public void add(ResultBasic resultBasic) {
        checkIllegalState(ResultType.LIST, "resultType must be LIST");
        basicList.add(resultBasic);
    }

    public ResultBasic get(int index) {
        checkIllegalState(ResultType.LIST, "resultType must be LIST");
        return basicList.get(index);
    }

    /**
     * 从basicList中移除某条数据
     *
     * @param resultBasic 被移除的数据
     * @return
     */
    public boolean remove(ResultBasic resultBasic) {
        checkIllegalState(ResultType.LIST, "resultType must be LIST");
        return basicList.remove(resultBasic);
    }

    public int parseNumber() {
        try {
            return Integer.parseInt(resultText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Integer.MIN_VALUE;
    }

    public double parseDecimal() {
        try {
            return Double.parseDouble(resultText);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return Double.MIN_VALUE;
    }

    @ResultType
    public int resultType() {
        return resultType;
    }


    @NonNull
    public String resultText() {
        return resultText;
    }

    /**
     * 返回一个不可改变的List
     *
     * @return unmodifiableList
     */
    @NonNull
    public List<ResultBasic> basicList() {
        checkIllegalState(ResultType.LIST, "resultType must be LIST");
        return Collections.unmodifiableList(new ArrayList<>(basicList));
    }

    /**
     * 返回一个不可改变的Map
     *
     * @return unmodifiableMap
     */
    @NonNull
    public Map<String, ResultBasic> basicMap() {
        checkIllegalState(ResultType.MAP, "resultType must be MAP");
        return Collections.unmodifiableMap(new HashMap<>(basicMap));
    }

    @NonNull
    public String getDisplayText() {
        return displayText;
    }

    @Override
    public String toString() {
        String resultTypeStr = "[ resultType: " + resultType;
        if (resultType == ResultType.MAP) {
            return resultTypeStr + ", basicMap: " + basicMap + "]";
        } else if (resultType == ResultType.LIST) {
            return resultTypeStr + ", basicList: " + basicList + "]";
        } else {
            return resultTypeStr + ", resultText: " + resultText + "]";
        }
    }
}
