package com.hletong.hyc.model;

import java.util.List;

/**
 * Created by ddq on 2016/12/22.
 * 摘牌时获取的车辆信息
 */
public class TransporterData<T> {
    private List<T> list;
    private int total;
    private HeHe map;

    public List<T> getList() {
        return list;
    }

    public String getVehiclesAuth() {
        if (map != null)
            return map.getVehiclesAuth();
        return null;
    }

    private static class HeHe{
        private String isWrtr;
        private String vehiclesAuth;
        private int totalCount;

        public String getVehiclesAuth() {
            return vehiclesAuth;
        }
    }
}
