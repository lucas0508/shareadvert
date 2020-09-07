package com.qujiali.shareadvert.custom.dialog

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.text.Html
import android.text.SpannableString
import android.text.Spanned
import android.text.TextPaint
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import android.widget.TextView
import com.qujiali.shareadvert.R

class ConfirmAgreementDialog(private val mContext: Context) :
    Dialog(mContext) {
    private val mBuilder: AlertDialog.Builder
    private val mDialog: AlertDialog
    private val mView: View
    private val mMessage: TextView
    private val mWarnMessage: TextView
    private val mOk: TextView
    private val mCancel: TextView
    private var mConfirmCallback: ConfirmCallback? =
        null
    private val mDialogContent: LinearLayout

    //  Html.fromHtml("点击登录,即同意<font color=\"#0498f5\">《叫个工人用户协议》</font>");
    var ServiceAgreement = "<font color=\"#0498f5\">《服务协议》</font>"
    var privacyPolicy = "<font color=\"#0498f5\">《隐私政策》</font>"
    fun show(
        message: String,
        callback: ConfirmCallback?
    ) {
        mMessage.text = message
        //        mWarnMessage.setText(Html.fromHtml("请你务必审慎阅读、充分理解" + ServiceAgreement + "和" + privacyPolicy + "各条款，包括但不限于:为了向你提" +
//                "供地理位置等服务,我们需要收集您的定位信息，你可以在设置中查看、变更并管理你的授权。"));
        mConfirmCallback = callback
        showDialog()
        val spannableString = SpannableString(
            Html.fromHtml(
                "请你务必审慎阅读、充分理解" + ServiceAgreement + "和" + privacyPolicy + "各条款，包括但不限于:为了向你提" +
                        "供地理位置等服务,我们需要收集您的定位信息，你可以在设置中查看、变更并管理你的授权。"
            )
        )
        spannableString.setSpan(Clickable(
            View.OnClickListener {
                //点击代码
//                Intent intent = new Intent(mContext, WebActivity.class);//https://www.jiaogegongren.com
//                intent.putExtra(Constants.WEB_KEY_URL, GlobalConstants.AGREEMENT_URL);
//                intent.putExtra(Constants.WEB_KEY_FLAG, 1);
//                mContext.startActivity(intent);
            }), 13, 19, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        spannableString.setSpan(Clickable(
            View.OnClickListener {
                //                Intent intent = new Intent(mContext, WebActivity.class);//https://www.jiaogegongren.com/clause/privacypolicy.html
//                intent.putExtra(Constants.WEB_KEY_URL, GlobalConstants.PRIVACYPOLICY_URL);
//                intent.putExtra(Constants.WEB_KEY_FLAG, 1);
//                mContext.startActivity(intent);
            }), 20, 26, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        mWarnMessage.text = spannableString
        mWarnMessage.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun showDialog() {

        if (!mDialog.isShowing) {
            mDialog.show()
        }
        mView.startAnimation(
            AnimationUtils.loadAnimation(
                mContext,
                R.anim.alpha_in
            )
        )
        mDialogContent.startAnimation(
            AnimationUtils.loadAnimation(
                mContext,
                R.anim.alpha_in
            )
        )
    }

    private fun dismissDialog(type: Int) {
        val animation =
            AnimationUtils.loadAnimation(mContext, R.anim.alpha_out)
        mDialogContent.startAnimation(animation)
        mView.startAnimation(
            AnimationUtils.loadAnimation(
                mContext,
                R.anim.alpha_out
            )
        )
        animation.setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation) {}
            override fun onAnimationEnd(animation: Animation) {
                if (type == 1 && mConfirmCallback != null) mConfirmCallback!!.onOk()
                if (type == 0 && mConfirmCallback != null) mConfirmCallback!!.onCancel()
                mDialog.dismiss()
            }

            override fun onAnimationRepeat(animation: Animation) {}
        })
    }

    interface ConfirmCallback {
        fun onOk()
        fun onCancel()
    }

    internal inner class Clickable(private val mListener: View.OnClickListener) :
        ClickableSpan(), View.OnClickListener {
        override fun updateDrawState(ds: TextPaint) {
            //设置是否有下划线
            ds.isUnderlineText = false
        }

        override fun onClick(v: View) {
            mListener.onClick(v)
        }

    }

    init {
        mBuilder = AlertDialog.Builder(mContext)
        mView = layoutInflater.inflate(R.layout.dialog_confirm_agreement, null)
        mDialogContent = mView.findViewById(R.id.ll_dialog_content)
        mMessage = mView.findViewById(R.id.tv_message)
        mWarnMessage = mView.findViewById(R.id.tv_warn_message)
        mOk = mView.findViewById(R.id.b_ok)
        mOk.setOnClickListener { dismissDialog(1) }
        mCancel = mView.findViewById(R.id.b_cancel)
        mCancel.setOnClickListener { dismissDialog(0) }
        mBuilder.setView(mView)
        mDialog = mBuilder.create()
        mDialog.setCanceledOnTouchOutside(false)
        mDialog.setCancelable(false)
        val w = mDialog.window
        w!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}