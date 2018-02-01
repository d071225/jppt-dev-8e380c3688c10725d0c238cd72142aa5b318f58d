package com.component.simple;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.OrientationHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.component.simple.presenter.NetDataPresenter;
import com.hletong.mob.architect.view.ListContract;
import com.hletong.mob.base.BaseRefreshFragment;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.io.File;
import java.util.List;

/**
 * Created by chengxin on 2017/3/22.
 */
public class RefreshFragment extends BaseRefreshFragment<String> implements ListContract.View<String> {

    @Override
    public void initView(Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        setPresenter(new NetDataPresenter(this,getActivity()));
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu_selectlist, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.list) {
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        } else if (item.getItemId() == R.id.grid) {
            mRecyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
            mRecyclerView.setAdapter(mAdapter);
        } else if (item.getItemId() == R.id.stagger) {
            mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(4, OrientationHelper.VERTICAL));
        }
        return true;
    }

    @Override
    public RecyclerView.ItemDecoration getItemDecoration() {
        return new SpaceItemDecoration(8);
    }

    @NonNull
    @Override
    public HFRecyclerAdapter<String> createAdapter() {
        return new DataAdapter(getContext(), null);
    }

    static class DataAdapter extends HFRecyclerAdapter<String> {

        public DataAdapter(Context context, List<String> data) {
            super(context, data);
        }

        @Override
        protected BaseHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
            return new BaseHolder(getInflater().inflate(R.layout.item_netdata, parent, false));
        }

        @Override
        protected void onBindItemViewHolder(BaseHolder holder, int position, String s) {
            File file=new File(s);
            Glide.with(getContext()).load(file).into((ImageView)holder.getView(R.id.imageView));
            holder.setText(R.id.tv_url, file.getName());
        }
    }
}
