package com.qujiali.shareadvert.module.other.view

import android.Manifest
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.qujiali.shareadvert.base.view.BaseActivity
import com.qujiali.shareadvert.custom.dialog.AppDownloadManager
import com.qujiali.shareadvert.module.home.view.HomeFragment

class AndroidOPermissionActivity : BaseActivity() {



    val INSTALL_PACKAGES_REQUESTCODE = 1
    private var mAlertDialog: AlertDialog? = null
//    var sListener: AppDownloadManager.AndroidOInstallPermissionListener? = null

    companion object {
        @JvmField
        var sListener: AppDownloadManager.AndroidOInstallPermissionListener? =null

        fun getInstance(): AndroidOPermissionActivity? {
            return AndroidOPermissionActivity()
        }
    }


    override fun getLayoutId(): Int = 0

    override fun initView() {
        super.initView()
        // 弹窗

        // 弹窗
        if (Build.VERSION.SDK_INT >= 26) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.REQUEST_INSTALL_PACKAGES), INSTALL_PACKAGES_REQUESTCODE
            )
        } else {
            finish()
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            INSTALL_PACKAGES_REQUESTCODE -> if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED
            ) {
                if (sListener != null) {
                    sListener!!.permissionSuccess()
                    finish()
                }
            } else {
                //startInstallPermissionSettingActivity();
                showDialog()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun showDialog() {
        val builder =
            AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK)
        builder.setTitle("")
        builder.setMessage("为了正常升级 “广告圈” APP，请点击设置按钮，允许安装未知来源应用，本功能只限用于 “广告圈” APP版本升级")
        builder.setPositiveButton(
            "设置"
        ) { dialogInterface, i ->
            startInstallPermissionSettingActivity()
            mAlertDialog!!.dismiss()
        }
        builder.setNegativeButton(
            "取消"
        ) { dialogInterface, i ->
            if (sListener != null) {
                sListener!!.permissionFail()
            }
            mAlertDialog!!.dismiss()
            finish()
        }
        mAlertDialog = builder.create()
        mAlertDialog!!.show()
        mAlertDialog!!.getButton(AlertDialog.BUTTON_POSITIVE)
            .setTextColor(Color.BLACK)
        mAlertDialog!!.getButton(DialogInterface.BUTTON_NEGATIVE)
            .setTextColor(Color.BLACK)
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private fun startInstallPermissionSettingActivity() {
        //注意这个是8.0新API
        val intent = Intent(
            Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
            Uri.parse("package:$packageName")
        )
        startActivityForResult(intent, 1)
    }

    override fun onActivityResult(
        requestCode: Int,
        resultCode: Int,
        data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // 授权成功
            if (sListener != null) {
                sListener!!.permissionSuccess()
            }
        } else {
            // 授权失败
            if (sListener != null) {
                sListener!!.permissionFail()
            }
        }
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        sListener = null
    }

}