package com.qujiali.shareadvert.custom.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;

public class VersionUpdateDialog {

    private Context mContext;
    private AppDownloadManager appDownloadManager;
    private AlertDialog.Builder builder;

    public VersionUpdateDialog(@NonNull Context context) {
        this.mContext = context;
        appDownloadManager = new AppDownloadManager((Activity) mContext);
    }

    /**
     * 显示提示更新对话框
     */
    public void showNoticeDialog(String status, final String url) {
        builder = new AlertDialog.Builder(mContext);

        builder.setTitle("检测到新版本！");
        builder.setMessage("广告圈更新咯\n优化了App各项功能，提高用户体验");
        if ("0".equals(status)) {//必須更新
            positivebtn(url, builder);
        } else {
            positivebtn(url, builder);
            builder.setNegativeButton("下次再说", (dialog, which) -> dialog.dismiss());
        }
        builder.setCancelable(false);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(DialogInterface.BUTTON_NEGATIVE).setTextColor(Color.BLACK);
    }

    private void positivebtn(final String url, AlertDialog.Builder builder) {
        builder.setPositiveButton("下载", (dialog, which) -> {
            appDownloadManager.setUpdateListener((currentByte, totalByte) -> {
                if ((currentByte == totalByte) && totalByte != 0) {
                    //updateDialog.dismiss();
                }
            });
            appDownloadManager.downloadApk(url, "广告圈", "优化了App各项功能，提高用户体验");
        });
    }
}
