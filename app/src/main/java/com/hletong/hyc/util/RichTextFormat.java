package com.hletong.hyc.util;

/**
 * Created by cx on 2016/11/4.
 */
public class RichTextFormat {
        public static String getUnderLine(String underLineStr){
                return "<u> "+underLineStr+" </u>";
        }

        public static String getUnderLineAndBold(String underLineStr){
                return "<b><u> "+underLineStr+" </u></b>";
        }

        public static String getUnderLineWithKey(String key,String underLineStr){
                return key+"："+ getUnderLine(underLineStr);
        }
}
