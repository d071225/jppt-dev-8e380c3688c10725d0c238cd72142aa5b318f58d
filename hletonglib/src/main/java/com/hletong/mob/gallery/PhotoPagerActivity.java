package com.hletong.mob.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.hletong.mob.R;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.gallery.adapter.PhotoPagerAdapter;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.builder.PreViewBuilder;

import java.util.ArrayList;
import java.util.Locale;

/**
 * 预览ViewPager
 * Created by cx on 15/6/24.
 */
public class PhotoPagerActivity extends BaseActivity {
    /******
     * 关联显示删除按钮
     *****/
    private int action;
    private ViewPager mViewPager;
    private PhotoPagerAdapter mPagerAdapter;
    private ArrayList<String> selectPaths;
    //用来判断列表是否有改变以决定是否返回刷新
    private ArrayList<String> copySelectPaths;
    private ArrayList<String> preViewPaths;
    private int mMaxCount;

    @Override
    public int getLayoutId() {
        return R.layout.__picker_activity_photo_pager;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        PreViewBuilder builder = PreViewBuilder.builder(getIntent().getExtras());
        int currentItem = builder.getCurrentItem();
        selectPaths = builder.getSelectPhotos();
        action = builder.getAction();
        if (!isPreView()) {
            copySelectPaths = new ArrayList<>(selectPaths);
        }
        preViewPaths = builder.getPreViewPhotos();
        mMaxCount = builder.getMaxPhotoCount();
        mViewPager = (ViewPager) findViewById(R.id.vp_photos);
        mPagerAdapter = new PhotoPagerAdapter(Glide.with(this), preViewPaths);
        mViewPager.setAdapter(mPagerAdapter);
        mViewPager.setCurrentItem(currentItem);
        mViewPager.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                refreshTitleView();
                if (action == PreViewBuilder.SELECT) {
                    supportInvalidateOptionsMenu();
                }
            }
        });
        refreshTitleView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (action == PreViewBuilder.DELETE) {
            getMenuInflater().inflate(R.menu.__picker_menu_preview, menu);
            return true;
        } else if (action == PreViewBuilder.SELECT) {
            getMenuInflater().inflate(R.menu.__picker_menu_select, menu);
            refreshCheckBox(menu);
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    private boolean toggle() {
        int position = mViewPager.getCurrentItem();
        String currentPath = preViewPaths.get(position);
        boolean isCheck = selectPaths.contains(currentPath);
        if (isCheck) {
            selectPaths.remove(currentPath);
        } else {
            if (selectPaths.size() == mMaxCount) {
                return false;
            } else {
                selectPaths.add(currentPath);
            }
        }
        return true;
    }

    private void delete() {
        int position = mViewPager.getCurrentItem();
        if (preViewPaths.size() > 0) {
            String deletePath = preViewPaths.remove(position);
            selectPaths.remove(deletePath);
            mPagerAdapter.notifyDataSetChanged();
            if (preViewPaths.size() == 0) {
                finish();
            }
        }
    }

    private void refreshTitleView() {
        String title = String.format(Locale.CHINA, "%s %s", getString(R.string.__picker_preview), getString(R.string.__picker_image_index, mViewPager.getCurrentItem() + 1,
                preViewPaths.size()));
        setCustomTitle(title);
    }

    private void refreshCheckBox(Menu menu) {
        int position = mViewPager.getCurrentItem();
        boolean isCheck = selectPaths.contains(preViewPaths.get(position));
        menu.getItem(0).setIcon(isCheck ? R.drawable.__picker_checkbox_sel : R.drawable.__picker_checkbox_nor);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        if (action == PreViewBuilder.SELECT) {
            refreshCheckBox(menu);
            return true;
        }
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (action == PreViewBuilder.DELETE) {
            delete();
            refreshTitleView();
            return true;
        } else if (action == PreViewBuilder.SELECT) {
            if (toggle()) {
                supportInvalidateOptionsMenu();
            } else {
                Toast.makeText(this, "最多只能选" + mMaxCount + "张", Toast.LENGTH_LONG).show();
            }
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private boolean isPreView() {
        return action == PreViewBuilder.PREVIEW;
    }

    @Override
    public void finish() {
        /**没有图片了 自动返回**/
        if (!isPreView()) {
            boolean isChanged = true;
            if (copySelectPaths.size() == selectPaths.size()) {
                if (copySelectPaths.size() != 0) {
                    copySelectPaths.removeAll(selectPaths);
                    isChanged = copySelectPaths.size() != 0;
                } else {
                    isChanged = false;
                }
            }
            if (isChanged) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(PickerBuilder.KEY_SELECTED_PHOTOS, selectPaths);
                setResult(RESULT_OK, intent);
            }
        }
        super.finish();
    }
}
