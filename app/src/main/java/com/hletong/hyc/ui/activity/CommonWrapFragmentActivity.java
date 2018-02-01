package com.hletong.hyc.ui.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v4.app.Fragment;

import com.hletong.hyc.R;
import com.hletong.mob.base.BaseActivity;
import com.hletong.mob.base.BaseFragment;

/**
 * 嵌套fragment的activity
 */
public class CommonWrapFragmentActivity extends BaseActivity {
    protected BaseFragment mBaseFragment;
    private static final String TITLE = "title";

    private static final String LAYOUT_ID = "layoutId";
    private static final String FRAGMENT_CLASSNAME = "fClassName";
    private static final String ARGUMENTS = "fragmentArguments";

    @Override
    public void initView(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.initView(savedInstanceState);
        mBaseFragment = (BaseFragment) getSupportFragmentManager().findFragmentByTag("BaseFragment");
        if (mBaseFragment == null) {
            mBaseFragment = (BaseFragment) Fragment.instantiate(this, getBundle().getString(FRAGMENT_CLASSNAME), getBundle().getBundle(ARGUMENTS));
            getSupportFragmentManager().beginTransaction().add(R.id.container, mBaseFragment, "BaseFragment").commit();
        }
        setCustomTitle(getBundle().getString(TITLE));
    }

    @Override
    public int getLayoutId() {
        return getBundle().getInt(LAYOUT_ID, R.layout.activity_common_list_fragment);
    }

    /**
     * @param layoutId 传递Activity布局ID
     * @return fClassName FRAGMENT 的类名
     */
    public static Bundle getBundle(String title, @LayoutRes int layoutId, Class<? extends BaseFragment> fClass, Bundle fragmentArguments) {
        Bundle bundle = new Bundle();
        bundle.putString(TITLE, title);
        bundle.putInt(LAYOUT_ID, layoutId);
        bundle.putString(FRAGMENT_CLASSNAME, fClass.getName());
        bundle.putBundle(ARGUMENTS, fragmentArguments);
        return bundle;
    }

    public static Bundle getBundle(String title, @LayoutRes int layoutId, Class<? extends BaseFragment> fClass) {
        return getBundle(title, layoutId, fClass, null);
    }

    public static Bundle getBundle(String title, Class<? extends BaseFragment> fClass) {
        return getBundle(title, R.layout.activity_common_list_fragment, fClass, null);
    }

    public static Bundle getBundle(String title, Class<? extends BaseFragment> fClass, Bundle arguments) {
        return getBundle(title, R.layout.activity_common_list_fragment, fClass, arguments);
    }

    public static Intent getIntentForTransAction(Context context, String title, Class<? extends BaseFragment> fClass, Bundle arguments) {
        Intent intent = new Intent(context, CommonWrapFragmentActivity.class);
        intent.putExtras(getBundle(title, fClass, arguments));
        return intent;
    }
}
