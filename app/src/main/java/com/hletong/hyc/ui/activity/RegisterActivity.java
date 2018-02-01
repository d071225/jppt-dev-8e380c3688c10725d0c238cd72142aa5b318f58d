package com.hletong.hyc.ui.activity;

import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hletong.hyc.R;
import com.hletong.hyc.enums.AppTypeConfig;
import com.hletong.hyc.ui.dialog.CallPhoneDialog;
import com.hletong.hyc.ui.fragment.CompanyRegisterFragment;
import com.hletong.mob.base.BaseViewPagerActivity;
import com.xcheng.view.adapter.TabInfo;
import com.xcheng.view.util.LocalDisplay;

import java.util.List;

public class RegisterActivity extends BaseViewPagerActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_switch;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.phone_selector, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        CallPhoneDialog.getInstance().show(getSupportFragmentManager());
        return true;
    }


    @Override
    public void getTabInfos(List<TabInfo> tabInfos) {
        Class<? extends Fragment> clazz = AppTypeConfig.getRegisterPersonFragmentClazz();
        if (AppTypeConfig.isCargo()) {
            tabInfos.add(new TabInfo(CompanyRegisterFragment.class.getSimpleName(), getString(R.string.companyRegister), R.drawable.nav_left_tab_selector, CompanyRegisterFragment.class));
            tabInfos.add(new TabInfo(clazz.getSimpleName(), getString(R.string.personalRegister), R.drawable.nav_right_tab_selector, clazz));
        } else {
            tabInfos.add(new TabInfo(clazz.getSimpleName(), getString(R.string.personalRegister), R.drawable.nav_left_tab_selector, clazz));
            tabInfos.add(new TabInfo(CompanyRegisterFragment.class.getSimpleName(), getString(R.string.companyRegister), R.drawable.nav_right_tab_selector, CompanyRegisterFragment.class));
        }
    }

    @NonNull
    @Override
    public View createTabView(int position, TabInfo tabInfo) {
        TextView tabView = new TextView(this);
        tabView.setTextSize(14);
        tabView.setText(tabInfo.title);
        tabView.setBackgroundResource(tabInfo.bgResId);
        tabView.setTextColor(ContextCompat.getColorStateList(this, R.color.nav_tab_color));
        tabView.setPadding(LocalDisplay.dp2px(12), 0, LocalDisplay.dp2px(12), 0);
        tabView.setGravity(Gravity.CENTER);
        return tabView;
    }
}
