package com.hletong.hyc.ui.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;

import com.hletong.hyc.BuildConfig;
import com.hletong.hyc.R;
import com.hletong.mob.architect.error.BaseError;
import com.hletong.mob.dialog.selector.BottomSelectorDialog;
import com.hletong.mob.dialog.selector.IItemShow;
import com.hletong.hyc.util.DialogFactory;

/**
 * Created by ddq on 2017/2/6.
 * 选择车辆（船舶）的基类
 */

public abstract class TransporterSelectorDialog<T extends IItemShow> extends BottomSelectorDialog<T> {

    public TransporterSelectorDialog(Context context) {
        super(context);
    }

    private void dialogAlert(String title, String errorMessage) {
        DialogFactory.showAlert(getContext(),
                title,
                errorMessage == null ? BuildConfig.transporter_empty_data : errorMessage,
                getString(R.string.ok),
                false,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getActivity().finish();
                    }
                });
    }

    @Override
    public void handleError(@NonNull BaseError error) {
        String title = null;
        if (getArguments() != null) {
            title = getArguments().getString("dialog_title");
        }
        if (error.isEmptyError()) {
            dialogAlert(title, BuildConfig.transporter_empty_data);
        } else if (error.isBusiError()) {
            dialogAlert(title, error.getMessage());
        } else {
            super.handleError(error);
        }
    }
}
