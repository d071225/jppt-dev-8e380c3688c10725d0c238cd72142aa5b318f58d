package com.hletong.hyc.contract;

import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.contract.ListContract;

/**
 * Created by ddq on 2017/4/1.
 */

public interface UpcomingContract {
    interface View extends ListContract.View<Object>{
        void success(Action action);
    }

    interface Presenter extends ListContract.Presenter<Object>{
        void collect(String tradeUuid,boolean collected);
        boolean isCollected(String tradeUuid);
    }

    interface LocalDataSource extends DataSource{
        void addToCollection(String tradeUuid);
        boolean isCollected(String tradeUuid);
    }

    class Action{
        String tradeUuid;
        boolean collect;

        public Action(String tradeUuid, boolean collect) {
            this.tradeUuid = tradeUuid;
            this.collect = collect;
        }

        public String getMessage() {
            if (collect)
                return "收藏成功";
            return "删除成功";
        }

        public String getTradeUuid() {
            return tradeUuid;
        }

        public boolean isCollect() {
            return collect;
        }
    }
}
