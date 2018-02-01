package com.hletong.hyc.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.adapter.PaperPhotoAdapter;
import com.hletong.hyc.contract.InfoCompleteContract;
import com.hletong.hyc.model.PaperPhoto;
import com.hletong.hyc.model.RegisterPhoto;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.util.Constant;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.pullrefresh.SpaceItemDecoration;
import com.xcheng.view.util.LocalDisplay;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通用的证件信息补全页面
 * 车主版个人信息补全
 * Created by chengxin on 2017/6/12.
 */
public class PapersAddActivity extends BaseActivity implements InfoCompleteContract.View {
    @BindView(R.id.id_recyclerView)
    RecyclerView mPapersRecyclerView;
    private BottomSelectorDialog<PaperPhoto> mBottomSelectorDialog;
    private PaperPhotoAdapter mPaperPhotoAdapter;
    private ArrayList<PaperPhoto> mPaperPhotos;
    private boolean isSingle;

    @Override
    public int getLayoutId() {
        return R.layout.activity_papers_add;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
        isSingle = getBundle().getBoolean(PaperPhoto.KET_SINGLE, false);
        mPaperPhotos = getSerializable(PaperPhoto.class.getSimpleName());
        mBottomSelectorDialog = new BottomSelectorDialog<PaperPhoto>(this) {
            @Override
            protected String getTitle() {
                return "选择上传的证件类型";
            }

            @Override
            protected void onLoad() {
                showList(mPaperPhotos, true);
            }
        };
        mBottomSelectorDialog.setOnItemSelected(new BottomSelectorDialog.OnItemSelectedListener<PaperPhoto>() {
            @Override
            public void onItemSelected(final PaperPhoto item, int extra) {
                if (isSingle) {
                    if (mPaperPhotoAdapter.getDataCount() > 0) {
                        showMessage("只需选择一种证件");
                        return;
                    }
                }
                for (PaperPhoto temp : mPaperPhotos) {
                    if (temp.getPaperType() == item.getPaperType() && temp.isInList()) {
                        showMessage("该证件已选择");
                        return;
                    }
                }

                item.setInList(true);
                mPaperPhotoAdapter.add(item);
            }
        });
        List<PaperPhoto> hasAdds = new ArrayList<>();
        for (PaperPhoto paper : mPaperPhotos) {
            if (paper.canShow()) {
                paper.setInList(true);
                hasAdds.add(paper);
            }
        }
        mPapersRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mPapersRecyclerView.addItemDecoration(new SpaceItemDecoration(LocalDisplay.dp2px(4)));
        mPaperPhotoAdapter = new PaperPhotoAdapter(this, hasAdds);
        mPapersRecyclerView.setAdapter(mPaperPhotoAdapter);
    }


    @OnClick({R.id.cv_papers, R.id.btn_commit})
    @Override
    public void onClick(View v) {
        super.onClick(v);
        if (R.id.cv_papers == v.getId()) {
            mBottomSelectorDialog.show();
        } else if (R.id.btn_commit == v.getId()) {
            boolean hasPaper = false;
            for (PaperPhoto info : mPaperPhotoAdapter.getData()) {
                if (info.isInList()) {
                    if (!info.isDataComplete()) {
                        showMessage(info.getEmptyDataMsg());
                        return;
                    } else {
                        hasPaper = true;
                    }
                }
            }
            if (!hasPaper) {
                showMessage("至少选择一种证件");
                return;
            }
            Intent intent = new Intent();
            intent.putExtra(PaperPhoto.class.getSimpleName(), mPaperPhotos);
            setResult(RESULT_OK, intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.call) {
            CallPhoneDialog.getInstance().show(getSupportFragmentManager());
            return true;
        }
        return false;
    }

    @Override
    protected void onActivityResultOk(int requestCode, Intent data) {
        if (requestCode == Constant.PHOTO_ADD_REQUEST) {
            RegisterPhoto registerPhoto = (RegisterPhoto) data.getSerializableExtra(RegisterPhoto.class.getSimpleName());
            int index = registerPhoto.getIndex();
            PaperPhoto paperPhoto = mPaperPhotoAdapter.getItem(index);
            paperPhoto.setRegisterPhoto(registerPhoto);
            mPaperPhotoAdapter.notifyItemChanged(index);
        }
    }

    @Override
    public void success(String message) {
        showMessage(message);
    }
}
