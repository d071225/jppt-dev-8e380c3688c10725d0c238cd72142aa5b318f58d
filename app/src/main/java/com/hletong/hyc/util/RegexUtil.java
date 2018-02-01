package com.hletong.hyc.util;

import com.hletong.mob.util.StringUtil;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by cx on 2016/10/24.
 */
public class RegexUtil {

    /**
     * 是否有空格
     *
     * @param input 检查的字符串
     * @return
     */
    public static boolean containEmpty(String input) {
        Pattern p = Pattern.compile("[\\s]+");
        Matcher match = p.matcher(input);
        return match.find();
    }

    /**
     * 是否是车牌号码
     *
     * @param input 检查的字符串
     * @return
     */
    public static boolean isCarNumber(String input) {
        Pattern p = Pattern.compile("^[\\u4e00-\\u9fa5]{1}[A-Z]{1}[A-Z_0-9]{5}$");
        Matcher match = p.matcher(input);
        return match.matches();
    }

    /**
     * 是否为手机号码
     *
     * @param input 检查的字符串
     * @return
     */
    public static boolean isPhoneNumber(String input) {
//        Pattern p = Pattern.compile("^(1[358479]\\d{9})$");
//        Matcher match = p.matcher(input);
//        return match.matches();
        return StringUtil.isMobileNumber(input);
    }

    /**
     * 密码验证 6-20位数字或字母组成
     *
     * @param input 检查的字符串
     * @return
     */
    public static boolean isPassword(String input) {
        Pattern p = Pattern.compile("^[a-zA-z0-9]{6,20}$");
        Matcher match = p.matcher(input);
        return match.matches();
    }

}
