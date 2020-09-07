package com.qujiali.shareadvert.custom.dialog;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.qujiali.shareadvert.R;


/**
 * @author QiZai
 * @desc
 * @date 2018/5/2 11:12
 */

public class ConfirmDialog extends Dialog {
    private Context mContext;
    private AlertDialog.Builder mBuilder;
    private AlertDialog mDialog;
    private View mView;
    private TextView mMessage, mWarnMessage, mOk, mCancel;
    private ConfirmCallback mConfirmCallback;
    private LinearLayout mDialogContent;

    public ConfirmDialog(@NonNull Context context) {
        super(context);
        this.mContext = context;
        mBuilder = new AlertDialog.Builder(mContext);
        mView = getLayoutInflater().inflate(R.layout.dialog_confirm, null);
        mDialogContent = mView.findViewById(R.id.ll_dialog_content);
        mMessage = mView.findViewById(R.id.tv_message);
        mWarnMessage = mView.findViewById(R.id.tv_warn_message);
        mOk = mView.findViewById(R.id.b_ok);
        mOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(1);
            }
        });

        mCancel = mView.findViewById(R.id.b_cancel);
        mCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismissDialog(0);
            }
        });

        mBuilder.setView(mView);

        mDialog = mBuilder.create();
        mDialog.setCanceledOnTouchOutside(false);
        Window w = mDialog.getWindow();
        w.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
    }

    /**
     *
     * @param message
     * @param callback
     */
    public void show(@NonNull String message, ConfirmCallback callback) {
        show(message, "确定", "取消", callback);
    }

    /**
     *
     * @param message
     * @param callback
     */
    public void showgo(@NonNull String message, ConfirmCallback callback) {
        show(message, "去认证", "取消", callback);
    }

    /**
     *
     * @param message
     * @param warnText
     * @param callback
     */
    public void show(@NonNull String message, String warnText, ConfirmCallback callback) {
        show(message, warnText, "确定", "取消", callback);
    }

    /**
     *
     * @param message
     * @param okText
     * @param cancelText
     * @param callback
     */
    public void show(@NonNull String message, String okText, String cancelText, ConfirmCallback callback) {
        show(message, null, okText, cancelText, callback);
    }

    /**
     *
     * @param message
     * @param warnMessage
     * @param okText
     * @param cancelText
     * @param callback
     */
    public void show(@NonNull String message, String warnMessage, String okText, String cancelText, ConfirmCallback callback) {
        mMessage.setText(message);
        mWarnMessage.setText(warnMessage);
        mOk.setText(okText);
        mCancel.setText(cancelText);
        mConfirmCallback = callback;
        showDialog();
    }

    private void showDialog() {
        mDialog.show();
        mView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_in));
        mDialogContent.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_in));
    }

    private void dismissDialog(final int type) {
        Animation animation = AnimationUtils.loadAnimation(mContext, R.anim.alpha_out);
        mDialogContent.startAnimation(animation);
        mView.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.alpha_out));
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (type == 1 && mConfirmCallback != null)
                    mConfirmCallback.onOk();
                if (type == 0 && mConfirmCallback != null)
                    mConfirmCallback.onCancel();
                mDialog.dismiss();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });
    }

    public interface ConfirmCallback {
        void onOk();

        void onCancel();
    }
}
