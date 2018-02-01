package com.hletong.hyc.contract;

import android.os.Bundle;

import com.hletong.hyc.model.Source;
import com.hletong.hyc.model.SourceTrack;
import com.hletong.hyc.model.SourceTrade;
import com.hletong.mob.architect.presenter.IBasePresenter;
import com.hletong.mob.architect.requestvalue.ItemRequestValue;
import com.hletong.mob.architect.contract.ListContract;

import org.json.JSONException;

import java.util.List;

/**
 * Created by ddq on 2017/3/8.
 */

public interface SourceTrackContract {
    interface View extends ListContract.View<SourceTrack> {
        void initSourceView(Source source) throws JSONException;

        void notifyItemInsertRange(int from, int count);

        void notifyItemRemoveRange(int from, int count);

        void notifyItemChanged(int index);
    }

    interface Presenter extends IBasePresenter {
        void loadTrackInfo(ItemRequestValue<SourceTrade> requestValue);

        void expandData(List<SourceTrack> data, SourceTrack.Status parent);

        void shrinkData(List<SourceTrack> data, SourceTrack.Status parent);

        void prepareData(Bundle bundle);
    }
}
