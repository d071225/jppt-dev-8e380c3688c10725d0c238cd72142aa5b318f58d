package com.hletong.hyc.contract;

import com.hletong.hyc.model.Draft;
import com.hletong.hyc.model.Source;
import com.hletong.mob.architect.model.CommonResult;
import com.hletong.mob.architect.model.DataSource;
import com.hletong.mob.architect.model.repository.DataCallback;
import com.hletong.mob.architect.view.ICommitSuccessView;
import com.hletong.mob.architect.contract.ListContract;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ddq on 2017/4/6.
 */

public interface DraftContract {
    interface LocalDataSource extends DataSource {
        void loadItem(String cargoUuid, DataCallback<Draft> callback);

        void loadList(int start, int limit, DataCallback<List<Draft>> callback);

        void modify(Source Draft, DataCallback<CommonResult> callback);

        void delete(ArrayList<Draft> sources, DataCallback<CommonResult> callback);
    }

    interface View extends ListContract.View<Draft>, ICommitSuccessView {
    }

    interface Presenter extends ListContract.Presenter<Draft> {
        void modify(Draft draft);

        void delete(ArrayList<Draft> sources);
    }
}
