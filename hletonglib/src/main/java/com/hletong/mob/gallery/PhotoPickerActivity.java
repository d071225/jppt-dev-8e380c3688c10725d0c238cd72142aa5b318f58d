package com.hletong.mob.gallery;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.Menu;
import android.view.MenuItem;

import com.hletong.mob.R;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.gallery.builder.PickerBuilder;
import com.hletong.mob.gallery.fragment.PhotoPickerFragment;
import com.xcheng.view.util.ToastLess;

import java.util.ArrayList;

import static com.hletong.mob.gallery.builder.PickerBuilder.KEY_SELECTED_PHOTOS;

public class PhotoPickerActivity extends BaseActivity {

    private PhotoPickerFragment pickerFragment;
    private int maxCount;
    ArrayList<String> oriSelectedPhotos;
    private int mSelectCount;

    @Override
    public int getLayoutId() {
        return R.layout.__picker_activity_photo_picker;
    }

    @Override
    public void initData() {
        super.initData();
        PickerBuilder builder = PickerBuilder.builder(getIntent().getExtras());
        maxCount = builder.getMaxPhotoCount();
        oriSelectedPhotos = builder.getSelectPhotos();
        mSelectCount = oriSelectedPhotos.size();
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        pickerFragment = (PhotoPickerFragment) getSupportFragmentManager().findFragmentByTag("PhotoPickerFragment");
        if (pickerFragment == null) {
            pickerFragment = PhotoPickerFragment
                    .newInstance(getIntent().getExtras());
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.container, pickerFragment, "PhotoPickerFragment")
                    .commit();
            getSupportFragmentManager().executePendingTransactions();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.__picker_menu_picker, menu);
        MenuItem item = menu.getItem(0);
        item.setTitle(getString(R.string.__picker_done_with_count, mSelectCount, maxCount));
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem item = menu.getItem(0);
        item.setTitle(getString(R.string.__picker_done_with_count, mSelectCount, maxCount));
        return true;
    }

    public void supportInvalidateOptionsMenu(int selectCount) {
        mSelectCount = selectCount;
        super.supportInvalidateOptionsMenu();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<String> selectPhotos = pickerFragment.getPhotoPickerAdapter().getSelectedPhotos();
        if (selectPhotos.size() != 0) {
            boolean isChanged = true;
            if (oriSelectedPhotos.size() == selectPhotos.size()) {
                if (oriSelectedPhotos.size() != 0) {
                    oriSelectedPhotos.removeAll(selectPhotos);
                    isChanged = oriSelectedPhotos.size() != 0;
                } else {
                    isChanged = false;
                }
            }
            if (isChanged) {
                Intent intent = new Intent();
                intent.putStringArrayListExtra(KEY_SELECTED_PHOTOS, selectPhotos);
                setResult(RESULT_OK, intent);
            }
            finish();
        } else {
            ToastLess.showToast("还没有选择图片");
        }
        return true;
    }
}
