package com.hletong.hyc.ui.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.view.View;

import com.hletong.hyc.R;
import com.hletong.hyc.util.DialogFactory;
import com.hletong.hyc.util.IOUtil;
import com.xcheng.permission.DeniedResult;
import com.xcheng.permission.EasyPermission;
import com.xcheng.permission.OnRequestCallback;
import com.xcheng.view.controller.EasyActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class ETCIntroducePaperActivity extends EasyActivity {

    @Override
    public int getLayoutId() {
        return R.layout.activity_etc_introduce_papaer;
    }

    @Override
    public void initView(@Nullable Bundle savedInstanceState) {
        super.initView(savedInstanceState);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.ev_id_submit})
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ev_id_submit:
                EasyPermission.with(this).permissions(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        .request(1228, new OnRequestCallback() {
                            @Override
                            public void onAllowed() {
                                downLoadIntroduce();
                            }

                            @Override
                            public void onRefused(DeniedResult deniedResult) {
                                if (deniedResult.allNeverAsked) {
                                    DialogFactory.permissionSetting(ETCIntroducePaperActivity.this, deniedResult.deniedPerms);
                                }
                            }
                        });
                break;
        }
    }

    private void downLoadIntroduce() {
        if (!Environment.getExternalStorageState()
                .equals(android.os.Environment.MEDIA_MOUNTED)) {
            showMessage("未找到sd卡");
            return;
        }
        File introFile = getPdfFile();
        if (introFile.exists()) {
            showDialog();
            return;
        }
        //得到资源中的Raw数据流
        InputStream in = getResources().openRawResource(R.raw.introduce);
        FileOutputStream fos = null;
        try {
            if (!introFile.createNewFile()) {
                throw new IOException("failed to create file:" + introFile.getPath());
            }
            fos = new FileOutputStream(introFile);
            byte[] buffer = new byte[8096];
            int last = 0, c, ct = 0;
            while ((c = in.read(buffer)) != -1) {
                ct += c;
                fos.write(buffer, 0, c);
            }
            showDialog();
        } catch (IOException e) {
            showMessage("下载失败");
            e.printStackTrace();
        } finally {
            IOUtil.close(fos);
        }
    }

    private File getPdfFile() {
        return new File(Environment.getExternalStorageDirectory(), "ETC_介绍信.pdf");
    }

    private void showDialog() {
        DialogFactory.showAlert(this, "介绍信下载成功", "文件路径：" + getPdfFile().getPath(), "确定", false, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });
    }
}
