package com.hletong.hyc.model;

import com.hletong.mob.util.NumberUtil;
import com.hletong.mob.util.SimpleDate;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

/**
 * Created by ddq on 2016/12/20.
 */
public class CBItem<T> {
    private List<T> bidRespDtos;
    private String endDttm;
    private double remQt;

    /**
     * 这里获取的数据是年月日时分秒,不是1970年至今的毫秒数,要进行转换
     * @return 毫秒数
     */
    public long getEndDttm() {
        try {
            Date date = SimpleDate.formatterHlet.parse(endDttm);
            return date.getTime();
        } catch (ParseException mE) {
            mE.printStackTrace();
        }
        return 0;
    }

    public String getRemQt() {
        return NumberUtil.format3f(remQt);
    }

    public List<T> getBidRespDtos() {
//        if (bidRespDtos != null) {
//            final int size = bidRespDtos.size();
//            if (size < 8) {
//                for (int i = 0; i < 8 - size; i++) {
//                    bidRespDtos.add(new CBRoundItem(true));
//                }
//            }
//        }
        return bidRespDtos;
    }
}
