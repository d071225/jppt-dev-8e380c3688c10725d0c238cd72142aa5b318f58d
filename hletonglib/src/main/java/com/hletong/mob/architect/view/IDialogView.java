package com.hletong.mob.architect.view;

import android.content.DialogInterface;

/**
 * Created by dongdaqing on 2017/7/12.
 */

public interface IDialogView {
    void showDialog(boolean cancelable, String title, String content, String p, String n, DialogInterface.OnClickListener pl, DialogInterface.OnClickListener nl);

    void showDialog(String title, String content, String p, DialogInterface.OnClickListener pl);

    void showDialog(String title, String content, DialogInterface.OnClickListener pl);
}
