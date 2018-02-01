package com.hletong.mob.util;

import java.util.regex.Pattern;

/**
 * Created by ddq on 2017/2/4.
 */

public class IdentityNoParseUtil {
    public static class Result {
        private int sex = 0;//0-男，1-女
        private SimpleDate birthday;

        public void setBirthday(SimpleDate birthday) {
            this.birthday = birthday;
        }

        public void setSex(int sex) {
            this.sex = sex;
        }

        public int getSex() {
            return sex;
        }

        public SimpleDate getBirthday() {
            return birthday;
        }
    }

    public static Result parseIdentityNo(String certificateNo) {
        Result result = new Result();
        if (certificateNo == null)
            return result;

        String myRegExpIDCardNo = "^\\d{6}(((19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}([0-9]|x|X))|(\\d{2}(0[1-9]|1[0-2])(0[1-9]|[1-2][0-9]|3[0-1])\\d{3}))$";
        boolean valid = Pattern.matches(myRegExpIDCardNo, certificateNo) || (certificateNo.length() == 17 && Pattern.matches(myRegExpIDCardNo, certificateNo.substring(0, 15)));
        if (!valid) {
            return result;
        }

        int idxSexStart = 16;
        int birthYearSpan = 4;
        //如果是15位的证件号码
        if (certificateNo.length() == 15) {
            idxSexStart = 14;
            birthYearSpan = 2;
        }

        //性别
        String idxSexStr = certificateNo.substring(idxSexStart, idxSexStart + 1);
        int idxSex = Integer.parseInt(idxSexStr) % 2;
        result.setSex((idxSex == 1) ? 0 : 1);

        //出生日期
        String year = (birthYearSpan == 2 ? "19" : "") + certificateNo.substring(6, 6 + birthYearSpan);
        String month = certificateNo.substring(6 + birthYearSpan, 6 + birthYearSpan + 2);
        String day = certificateNo.substring(8 + birthYearSpan, 8 + birthYearSpan + 2);
        result.setBirthday(new SimpleDate(Integer.parseInt(year), Integer.parseInt(month), Integer.parseInt(day)));

        return result;
    }
}
