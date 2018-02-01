package com.hletong.hyc.model;

import java.util.List;

/**
 * Created by chengxin on 2016/10/20.
 */
public class BaseListResult<T>{
    private int total;
    private List<T> list;
    private MapEntity map;

    public MapEntity getMap() {
        return map;
    }

    public void setMap(MapEntity map) {
        this.map = map;
    }

    public List<T> getList() {
        return list;
    }

    public void setList(List<T> list) {
        this.list = list;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public static class MapEntity {
        /**
         * totalCount : -1
         */
        private int totalCount;

        public void setTotalCount(int totalCount) {
            this.totalCount = totalCount;
        }

        public int getTotalCount() {
            return totalCount;
        }
    }
}
