package com.hletong.hyc.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

/**
 * Created by dongdaqing on 2017/8/1.
 */

public class CargoPermission {
    private PaperAudit2 paper_aduit;
    private LinkedHashMap<String, AuthInfo> mm_trade_auth;

    private static class PaperAudit {
        private String paper_aduit;
        private String paper_aduit_msg;
    }

    private static class PaperAudit2 {
        private PaperAudit paper_aduit;
    }

    public String getReviewMessage() {
        return paper_aduit.paper_aduit.paper_aduit_msg;
    }

    public List<TradeType> getData() {
        List<TradeType> list = new ArrayList<>();
        list.add(TradeType.getPlatform(mm_trade_auth.get("PLATFORM"), "1".equals(paper_aduit.paper_aduit.paper_aduit), getReviewMessage()));
        list.add(TradeType.getOneSelf(mm_trade_auth.get("ONESELF")));
        list.add(TradeType.getSelfTrade(mm_trade_auth.get("SELF_TRADE")));
        return list;
    }
}
