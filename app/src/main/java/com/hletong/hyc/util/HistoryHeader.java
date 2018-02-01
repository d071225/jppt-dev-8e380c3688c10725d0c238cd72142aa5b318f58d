package com.hletong.hyc.util;

import android.content.res.Resources;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.SourceListAdapter;
import com.hletong.mob.util.SimpleDate;
import com.xcheng.view.util.LocalDisplay;

public class HistoryHeader {
    private TextView tv;
    private String history;

    public HistoryHeader(ViewGroup mRoot,int index) {
        tv = (TextView) View.inflate(mRoot.getContext(), R.layout.activity_header_time, null);
        tv.setVisibility(View.GONE);
        ViewGroup.LayoutParams Params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,  LocalDisplay.dp2px( 20));
        mRoot.addView(tv, index, Params);
        setHeader(mRoot);
    }

    private void setHeader(ViewGroup mRoot) {
        RecyclerView recyclerView= (RecyclerView) mRoot.findViewById(R.id.id_recyclerView);
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int position = linearLayoutManager.findFirstVisibleItemPosition();
                RecyclerView.ViewHolder mViewHolder = recyclerView.findViewHolderForAdapterPosition(position);
                if (mViewHolder instanceof SourceListAdapter.BaseViewHolder){
                    SourceListAdapter.BaseViewHolder baseHolder = (SourceListAdapter.BaseViewHolder) mViewHolder;
                    setHeaderText(baseHolder.getTime());
                }
            }
        });
    }

    private void setHeaderText(String headerTime) {
        if (headerTime == null)
            return;

        if (!headerTime.equals(history)){
            history = headerTime;
            SimpleDate parse = SimpleDate.parse(history);
            Resources resources = tv.getResources();
            String time = parse.getDateDescription(resources);
            tv.setVisibility(View.VISIBLE);
            tv.setText(time);
        }
    }
}
