package com.hletong.mob.util;

import android.annotation.SuppressLint;

import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static String EMPTY_STRING = "";
    public static String TIME_FORMAT = "yyyy-MM-dd HH:mm:ss"; // 长日期格式
    final static String PLEASE_SELECT = "请选择...";

    /**
     * 验证座机号码和传真号码. 区号有3-4位（第一位数字是0，后面有2位或3位数字）； 然后可以以横杠连接（横杠可有可无）后面的7-8位电话号码，
     * 若后面还有分机号（1-4位数字），则前面加横杠连接。
     *
     * @param fax
     * @return
     */
    public static boolean isTelePhoneOrFax(String fax) {
        String regex = "^(0\\d{2}(-)?\\d{7,8}(-\\d{1,4})?)|(0\\d{3}(-)?\\d{7,8}(-\\d{1,4})?)$";
        return check(fax, regex);
    }

    /**
     * 使用Webkaifa/zzbds' target='_blank'>正则表达式进行表单验证
     */
    public static boolean check(String str, String regex) {
        boolean flag;
        try {
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(str);
            flag = matcher.matches();
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }

    /**
     * 验证邮政编码
     */
    public static boolean checkPost(String post) {
        return post.matches("[1-9]\\d{5}(?!\\d)");
    }

    /**
     * 如果为空 返回真
     *
     * @param str 字符串
     * @return 如果为空或长度等于零，返回真，其他返回假
     */
    public static boolean isBlank(String str) {
        return str == null || str.length() == 0;
    }

    public static boolean empty(Object o) {
        return o == null || "".equals(o.toString().trim())
                || "null".equalsIgnoreCase(o.toString().trim())
                || "undefined".equalsIgnoreCase(o.toString().trim())
                || PLEASE_SELECT.equals(o.toString().trim());
    }

    public static boolean notEmpty(Object o) {
        return o != null && !"".equals(o.toString().trim())
                && !"null".equalsIgnoreCase(o.toString().trim())
                && !"undefined".equalsIgnoreCase(o.toString().trim())
                && !PLEASE_SELECT.equals(o.toString().trim());
    }

    /**
     * 字符串为空，或去掉空格为空，则返回真
     *
     * @param str 字符串
     * @return 如果字符串为空, 或去掉空格长度为0, 返回真，其他返回假
     */
    public static boolean isTrimBlank(String str) {
        return str == null || str.trim().equals(EMPTY_STRING);
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @return String
     */
    public static String doEmpty(String str) {
        return doEmpty(str, "");
    }

    /**
     * 处理空字符串
     *
     * @param str
     * @param defaultValue
     * @return String
     */
    public static String doEmpty(String str, String defaultValue) {
        if (str == null || str.equalsIgnoreCase("null")
                || str.trim().equals("") || str.trim().equals("－请选择－")) {
            str = defaultValue;
        } else if (str.startsWith("null")) {
            str = str.substring(4, str.length());
        }
        return str.trim();
    }

    /**
     * 首字母大写
     *
     * @param str 要转换的字符串
     * @return 首字母大写的字符串
     */
    @SuppressLint("DefaultLocale")
    public static String capFirstUpperCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    /**
     * 首字母小写
     *
     * @param str 要转换的字符串
     * @return 首字母小写的字符串
     */
    @SuppressLint("DefaultLocale")
    public static String capFirstLowerCase(String str) {
        if (isBlank(str)) {
            return str;
        }
        return str.substring(0, 1).toLowerCase() + str.substring(1);
    }

    /**
     * 是否是身份证号码
     *
     * @param str
     * @return
     */
    public static boolean isIdCardNumber(String str) {
        Pattern p = Pattern
                .compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}[0-9xX]$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    /**
     * 确认密码对比
     *
     * @param str1
     * @param str2
     * @return
     */
    public static boolean isConfirmPassword(String str1, String str2) {
        return str1.equals(str2);
    }

    /**
     * 隐藏手机号码中间4位, example: 185****6666 事先判断传入字符串是否为手机号
     *
     * @param phone
     * @return
     */
    public static String hiddenPhoneNumPd(final String phone) {
        if (isMobileNumber(phone)) {
            char[] mobile = phone.toCharArray();
            for (int i = 3; i < 7; i++) {
                mobile[i] = '*';
            }
            return String.valueOf(mobile);
        }
        return "";
    }

    /**
     * 隐藏银行账号的后四位 example: 111111111111004 ---> 11111111111****
     *
     * @return
     */
    public static String hiddenBankAccount(final String str) {
        if (str != null) {
            char[] number = str.toCharArray();
            int len = number.length;
            for (int i = 0; i < len - 4; i++) {
                number[i] = '*';
            }
            return String.valueOf(number);
        } else {
            return "";
        }
    }

    /**
     * 隐藏所有
     *
     * @return
     */
    public static String hiddenAll(final String str) {
        if (str != null) {
            char[] number = str.toCharArray();
            int len = number.length;
            for (int i = 0; i < len; i++) {
                number[i] = '*';
            }
            return String.valueOf(number);
        } else {
            return "";
        }
    }

    /**
     * 只保留银行两个字 example: 招商工商西湖区 ---> **银行***
     *
     * @param str
     * @return
     */
    public static String hiddenBankName(final String str) {
        if (str != null) {
            if (!str.contains("银行")) {
                return hiddenAll(str);
            } else {
                String newStrLeft = str.substring(0, str.indexOf("银行"));
                String newStrRight = str.substring(str.indexOf("银行"),
                        str.length());
                return hiddenAll(newStrLeft) + "银行" + hiddenAll(newStrRight);
            }
        } else {
            return "";
        }
    }

    /**
     * 隐藏姓名的姓 example: 张三 ---> 张*
     *
     * @param str
     * @return
     */
    public static String hiddenName(final String str) {
        if (isTrimBlank(str)) {
            return "";
        } else {
            String temp = str.substring(0, 1);
            return String.valueOf(temp) + "**";
        }
    }

    /**
     * 隐藏身份证号的前面几位，只显示首末四位， example: 330882199204018224 ---> **************8224
     *
     * @param str
     * @return
     */
    public static String hiddenIdCard(final String str) {
        char[] number = str.toCharArray();
        int len = number.length;
        for (int i = 0; i < len - 4; i++) {
            number[i] = '*';
        }
        return String.valueOf(number);
    }

    /**
     * 输入的字符串每4位隔开并添加空格（比如银行卡号码等）
     */
    public static String add4blank(String str) {
        str = str.replace(" ", "");
        int strLength = str.length() / 4;
        String temp = "";
        for (int i = 0; i < strLength; i++) {
            temp += str.substring(i * 4, (i + 1) * 4);
            temp += " ";
        }
        temp += str.substring(strLength * 4);
        return temp;
    }

    /**
     * 手机号码3 4 4格式, example: 185 6666 6666
     */
    public static String addmobileblank(String str) {
        if (str.replace(" ", "").length() != 11)
            return str;
        String temp = "";
        temp += str.subSequence(0, 3);
        temp += ' ';
        temp += str.substring(3, 7);
        temp += ' ';
        temp += str.substring(7);
        return temp;
    }

    /**
     * 日期格式转换（Str转Str） 2014-05-06 转为 20140506
     */
    public static String formatDate2(String str) {
        return str.replace("-", "");
    }

    /**
     * 日期格式转换（Str转Str） 20140506转为2014-05-06
     */
    public static String formatDate(String str) {
        if (str.replace("-", "").length() != 8)
            return str;
        String temp = "";
        temp += str.subSequence(0, 4);
        temp += '-';
        temp += str.substring(4, 6);
        temp += '-';
        temp += str.substring(6);
        return temp;
    }

    /**
     * 时间格式转换（Str转Str） 123312转为12:33
     */
    public static String formatTime(String str) {
        String temp = "";
        temp += str.subSequence(0, 2);
        temp += ':';
        temp += str.substring(2, 4);
        return temp;
    }

    /**
     * 将长日期格式的字符串转换为长整型 1970-09-01 12:00:00
     *
     * @param date
     * @return
     */
    @SuppressLint("SimpleDateFormat")
    public static long convert2long(String date) {
        try {
            if (!isBlank(date)) {
                SimpleDateFormat sf = new SimpleDateFormat(TIME_FORMAT);
                return sf.parse(date).getTime();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0L;
    }

    public static boolean num(Object o) {
        int n = 0;
        try {
            n = Integer.parseInt(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n > 0;
    }

    public static boolean decimal(Object o) {
        double n = 0;
        try {
            n = Double.parseDouble(o.toString().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return n > 0.0;
    }

    /**
     * 保留小数点后两位数字，但不进行四舍五入
     */
    public static String sub2DecimalPlaceNoRoundOff(String strDouble) {
        String str;
        int position = strDouble.indexOf("."); // 计算小数点的位置
        if (position != -1) {
            if ((strDouble.length() - 1 - position) >= 2) {
                // 如果小数点后多于两位
                strDouble = strDouble.substring(0, position + 3);
                // } else {
                // // 小数点后不足两位补0
                // DecimalFormat df = new DecimalFormat("0.00");
                // strDouble = df.format(double1);
            }
        }
        return strDouble;
    }

    /**
     * 是否是手机字符串
     *
     * @param mobiles
     * @return
     */
    public static boolean isMobileNumber(String mobiles) {
//		Pattern p = Pattern
//				.compile("^1(([38]\\d{2})|(5[^4]\\d{1})|(7((0[059])|(7[678]))))\\d{7}$");
//		Matcher m = p.matcher(mobiles);
//		return m.matches();
        Pattern p = Pattern.compile("^(1[358479]\\d{9})$");
        Matcher match = p.matcher(mobiles);
        return match.matches();
    }

    /**
     * 判断是否是Email
     *
     * @return
     */
    public static boolean isEmail(String email) {
        Pattern emailPattern = Pattern
                .compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
        Matcher matcher = emailPattern.matcher(email);
        return matcher.find();
    }

    public static boolean is5Chinese(String str) {
        Pattern p = Pattern.compile("[\u4e00-\u9f5a]");
        int count = 0;
        Matcher m;
        char c;
        for (int i = 0; i < str.length(); i++) {
            c = str.charAt(i);
            m = p.matcher("" + c);
            if (m.matches()) {
                count++;
            }
        }
        return count > 4;
    }

    public static boolean isUrl(String url) {
        return "http://".equals(url) || "https://".equals(url);
    }

    /**
     * 判断输入的是不是中文
     */
    public static boolean isChinese(String input) {
        return input.matches("[\u4e00-\u9fa5]+");
    }

    /**
     * 税务识别号
     *
     * @return
     */
    public static boolean isTaxCode(String input) {
        Pattern pattern = Pattern.compile("^((\\d{15})|\\d{18})$");
        Matcher matcher = pattern.matcher(input);
        return matcher.matches();
    }
}
