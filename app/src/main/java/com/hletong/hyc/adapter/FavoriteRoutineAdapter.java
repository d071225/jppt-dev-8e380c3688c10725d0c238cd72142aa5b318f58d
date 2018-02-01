package com.hletong.hyc.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.contract.FavoriteRoutineContract;
import com.hletong.hyc.model.FavoriteRoutine;
import com.hletong.hyc.presenter.FavoriteRoutinePresenter;
import com.hletong.hyc.ui.activity.CommonWrapFragmentActivity;
import com.hletong.hyc.ui.fragment.SourceListFragmentFavorite;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.SimpleProgress;
import com.hletong.mob.architect.view.IProgress;
import com.hletong.mob.architect.view.ITransactionView;
import com.hletong.mob.pullrefresh.BaseHolder;
import com.hletong.mob.pullrefresh.HFRecyclerAdapter;

import java.util.Locale;

/**
 * Created by ddq on 2017/2/10.
 */

public class FavoriteRoutineAdapter extends HFRecyclerAdapter<FavoriteRoutine> {
    private ITransactionView mFragmentDelegate;
    private FavoriteRoutineContract.Presenter mPresenter;
    private IProgress mProgress;

    public FavoriteRoutineAdapter(Context context, FavoriteRoutineContract.View view, ITransactionView fragmentDelegate) {
        super(context, null);
        this.mFragmentDelegate = fragmentDelegate;
        mPresenter = new FavoriteRoutinePresenter(view);
        mProgress = new SimpleProgress(context);
    }

    @Override
    protected BaseHolder<FavoriteRoutine> onCreateItemViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.recycler_item_favorite_routine, parent, false));
    }

    private class ViewHolder extends BaseHolder<FavoriteRoutine> implements View.OnClickListener, View.OnLongClickListener {
        private FavoriteRoutine mFavoriteRoutine;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);
        }

        public void setData(FavoriteRoutine fr) {
            this.mFavoriteRoutine = fr;
            TextView textView = (TextView) itemView;
            textView.setText(fr.getFormatAddress());
        }

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(v.getContext(), CommonWrapFragmentActivity.class);
            intent.putExtras(CommonWrapFragmentActivity.getBundle(
                    mFavoriteRoutine.getActivityTitle(),
                    SourceListFragmentFavorite.class,
                    SourceListFragmentFavorite.getBundle(mFavoriteRoutine)));
            mFragmentDelegate.toActivity(intent, SourceListAdapter.BaseViewHolder.REQUEST_CODE_FOR_LIST_ITEM, null);
        }

        @Override
        public boolean onLongClick(View v) {
            DialogFactory.showAlertWithNegativeButton(v.getContext(), getString(R.string.delete_favorite_routine), String.format(Locale.CHINESE, getString(R.string.delete_favorite_routine_message), mFavoriteRoutine.getActivityTitle()), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    mPresenter.delete(mFavoriteRoutine.getRouteUuid(), mProgress);//删除常跑路线
                }
            });
            return true;
        }
    }
}
