package com.hletong.hyc.model;

import com.hletong.hyc.ui.dialog.DriverActionDialog;
import com.hletong.mob.util.SimpleDate;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * 好司机活动
 */
public class Action implements Serializable {
    //活动状态
    public static final String KEY_ACTION = "key_action";

    public static final int BEFORE_CARE_DRIVER = 1000;//活动开始前
    public static final int CARE_DRIVER_SIGN = 1001;//活动报名中
    public static final int CARE_DRIVER_VOTE = 1010;//活动投票中
    public static final int CARE_DRIVER_SIGN_CARE_DRIVER_VOTE = 1011;//报名中投票中
    public static final int CARE_DRIVER_END = 1111;//活动结束

    //是否能投票
    public static boolean canVote() {
        return DriverActionDialog.sActionCode == Action.CARE_DRIVER_SIGN_CARE_DRIVER_VOTE || DriverActionDialog.sActionCode == Action.CARE_DRIVER_VOTE;
    }

    private static final long serialVersionUID = -1742751394426948569L;
    private List<Notice> notices;
    private String nextUpdate;

    public List<Notice> getNotices() {
        return notices;
    }

    public static class Notice implements Serializable {

        private static final long serialVersionUID = -1343921448448501105L;
        //活动编号
        private String activity_no;
        //活动名称
        private String activity_name;
        //开始时间
        private String begin_dttm;
        //结束时间
        private String end_dttm;
        //活动状态
        private String status;
        //最后修改时间
        private String last_mod_ts;
        //活动联系人
        private String activity_content;
        //创建人
        private String create_opid;
        //创建时间
        private String create_ts;
        //最后修改人
        private String last_mod_opid;
        //版本号
        private String version;

        public String getActivity_no() {
            return activity_no;
        }

        public String getActivity_name() {
            return activity_name;
        }

        public String getBegin_dttm() {
            return begin_dttm;
        }

        public String getEnd_dttm() {
            return end_dttm;
        }

        public String getStatus() {
            return status;
        }

        public String getLast_mod_ts() {
            return last_mod_ts;
        }

        public String getActivity_content() {
            return activity_content;
        }

        public String getCreate_opid() {
            return create_opid;
        }

        public String getCreate_ts() {
            return create_ts;
        }

        public String getLast_mod_opid() {
            return last_mod_opid;
        }

        public String getVersion() {
            return version;
        }

        public String formatBegin() {
            Date date = SimpleDate.parseStrDate(begin_dttm, SimpleDate.formatterHlet);
            return SimpleDate.formatDate(date, new SimpleDateFormat("MM月dd日", Locale.getDefault()));
        }

        public String formatEnd() {
            Date date = SimpleDate.parseStrDate(end_dttm, SimpleDate.formatterHlet);
            return SimpleDate.formatDate(date, new SimpleDateFormat("MM月dd日", Locale.getDefault()));
        }
    }
}
