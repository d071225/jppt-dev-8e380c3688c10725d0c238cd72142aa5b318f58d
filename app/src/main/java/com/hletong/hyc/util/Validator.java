package com.hletong.hyc.util;

import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.ui.widget.CommonInputView;
import com.hletong.mob.architect.error.ErrorFactory;
import com.hletong.mob.architect.view.IBaseView;
import com.hletong.mob.util.SimpleDate;
import com.hletong.mob.util.StringUtil;

import java.util.Calendar;

/**
 * Created by dongdaqing on 2017/7/5.
 * 简化表单参数校验
 */

public class Validator {
    public static boolean isPhone(String s, IBaseView view) {
        if (!isNotNull(s, view, "电话不能为空"))
            return false;

        if (!StringUtil.isMobileNumber(s) && !StringUtil.isTelePhoneOrFax(s)) {
            view.handleError(ErrorFactory.getParamError("电话号码格式不正确"));
            return false;
        }
        return true;
    }

    /**
     * 检测数字是否大于0
     *
     * @param s            输入
     * @param view
     * @param nullMessage  内容为空时的提示内容
     * @param falseMessage 小于0时的提示内容
     * @return
     */
    public static boolean isAboveZero(String s, IBaseView view, String nullMessage, String falseMessage) {
        if (!isNotNull(s, view, nullMessage)) {
            return false;
        }

        try {
            return isAboveZero(Double.parseDouble(s), view, falseMessage);
        } catch (NumberFormatException e) {
            view.handleError(ErrorFactory.getParamError("输入的内容格式不正确"));
            return false;
        }
    }

    public static boolean isAboveZero(double s, IBaseView view, String falseMessage) {
        if (s <= 0) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    /**
     * 数字密码，长为6位
     *
     * @param s
     * @param view
     * @return
     */
    public static boolean isNumberPassword(String s, IBaseView view) {
        if (!isNotNull(s, view, "密码不能为空")) {
            return false;
        }
        if (s.length() != 6) {
            view.handleError(ErrorFactory.getParamError("密码必须为6位数字"));
            return false;
        }
        return true;
    }

    public static boolean isNotNull(Object s, IBaseView view, String message) {
        if (s == null) {
            view.handleError(ErrorFactory.getParamError(message));
            return false;
        }
        if (s instanceof String) {
            String ss = (String) s;
            if (ss.trim().length() == 0) {
                view.handleError(ErrorFactory.getParamError(message));
                return false;
            }
        }

        return true;
    }

    public static boolean isIdCard(String s, IBaseView view) {
        if (!isNotNull(s, view, "身份证不能为空")) {
            return false;
        }
        if (!StringUtil.isIdCardNumber(s)) {
            view.handleError(ErrorFactory.getParamError("身份证格式不正确"));
            return false;
        }
        return true;
    }

    public static boolean timeIsLateThanCurrent(SimpleDate sd, IBaseView view, String emptyMessage, String falseMessage) {
        if (!isNotNull(sd, view, emptyMessage)) {
            return false;
        }
        Calendar calendar = Calendar.getInstance();
        SimpleDate current = new SimpleDate(calendar);
        if (current.compareTo(sd) > 0) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    public static boolean isAllChinese(String s, IBaseView view, String nullMessage, String falseMessage) {
        if (!isNotNull(s, view, nullMessage)) {
            return false;
        }
        if (!StringUtil.isChinese(s)) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    public static boolean isSame(String s1, String s2, IBaseView view, String falseMessage) {
        if (!s1.equals(s2)) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    public static boolean isNotSame(String s1, String s2, IBaseView view, String falseMessage) {
        if (s1.equals(s2)) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    /*
     * c1 < c2
     */
    public static <T> boolean isSmallerThan(Comparable<T> c1, Comparable<T> c2, IBaseView view, String falseMessage) {
        if (c1.compareTo((T) c2) >= 0) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    /*
     * c1 <= c2
     */
    public static <T> boolean isNoLargerThan(Comparable<T> c1, Comparable<T> c2, IBaseView view, String falseMessage) {
        if (c1.compareTo((T) c2) > 0) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    public static boolean isTaxCode(String s, IBaseView view) {
        if (!isNotNull(s, view, "纳税识别号不能为空")) {
            return false;
        }
        if (!StringUtil.isTaxCode(s)) {
            view.handleError(ErrorFactory.getParamError("纳税识别号格式不正确"));
            return false;
        }
        return true;
    }

    public static boolean isLength(String s, int length, String falseMessage, IBaseView view) {
        return isLengthInRange(s, length, length, falseMessage, view);
    }

    public static boolean isLengthInRange(String s, int start, int end, String falseMessage, IBaseView view) {
        if (isNotNull(s, view, falseMessage)) {
            if (s.length() >= start && s.length() <= end) {
                return true;
            } else {
                view.handleError(ErrorFactory.getParamError(falseMessage));
                return false;
            }
        }
        return false;
    }

    public static boolean notContainEmptyCharacter(String s, IBaseView view, String falseMessage) {
        if (RegexUtil.containEmpty(s)) {
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }
        return true;
    }

    public static boolean isNotAllEmpty(Object[] objects, IBaseView view, String falseMessage) {
        boolean empty = true;
        for (int i = 0; i < objects.length; i++) {
            if (objects[i] != null) {
                if (objects[i] instanceof String) {
                    String s = (String) objects[i];
                    if (!TextUtils.isEmpty(s)){
                        empty = false;
                        break;
                    }
                } else {
                    empty = false;
                    break;
                }
            }
        }
        if (empty){
            view.handleError(ErrorFactory.getParamError(falseMessage));
            return false;
        }else {
            return true;
        }
    }
}
