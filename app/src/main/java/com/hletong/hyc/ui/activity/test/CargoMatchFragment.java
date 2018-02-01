package com.hletong.hyc.ui.activity.test;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.AppContract;
import com.hletong.hyc.model.UnTodo;
import com.hletong.hyc.ui.activity.AppGuideHelper;
import com.hletong.hyc.ui.fragment.AppFragment;

/**
 * Created by Daiyy on 2018/2/1.
 */

public class CargoMatchFragment extends AppFragment implements AppContract.View {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void setUnToDoInfo(UnTodo unTodo) {

    }

    @Override
    protected void autoAdFirstOpen() {

    }

    @Override
    protected void appGuideFirstOpen(AppGuideHelper guideHelper) {

    }

    @Override
    public int getLayoutId() {
        return  R.layout.fragment_app_hyh;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.phone_selector, menu);
        MenuItem menuItem = menu.findItem(R.id.call);
        menuItem.setTitle("筛选");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
     /*   if (mSourceFilterPanel == null)
            mSourceFilterPanel = new CargoSourceListFragment.SourceFilterPanel();
        mSourceFilterPanel.showFilter();*/
        return true;
    }
}
