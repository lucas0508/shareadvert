package com.qujiali.shareadvert.common.utils;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.text.TextUtils;
import com.qujiali.shareadvert.base.view.BaseApplication;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class DevicePermissionsUtils {
    private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
    private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";
    private static String mPhoneModel;
    private static String mSystemVersion;
    private static String mAppCurrentVersion;
    private static String mAppMaxVersion;
    private static String mAppMinVersion;


    /**
     * 获取手机型号
     *
     * @return
     */
    public static String getPhoneModel() {
        if (mPhoneModel == null)
            mPhoneModel = android.os.Build.MODEL;
        return mPhoneModel;
    }

    /**
     * 获取系统
     * ios / android / weixin
     *
     * @return
     */
    public static String getPhoneSystem() {
        return "android";
    }

    /**
     * 获取系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        if (mSystemVersion == null)
            mSystemVersion = android.os.Build.VERSION.RELEASE;
        return mSystemVersion;
    }

    /**
     * 获取App当前版本
     *
     * @return
     */
    public static String getAppCurrentVersion() {
        if (mAppCurrentVersion == null) {
            PackageManager packageManager = BaseApplication.instance.getPackageManager();
            PackageInfo packInfo;
            try {
                // getPackageName()是你当前类的包名，0代表是获取版本信息
                packInfo = packageManager.getPackageInfo(BaseApplication.instance.getPackageName(), 0);
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
                return "0.0";
            }
            mAppCurrentVersion = packInfo.versionName;

            String[] s = mAppCurrentVersion.split("\\.");
            mAppMaxVersion = s[0];
            mAppMinVersion = mAppCurrentVersion;
        }
        return mAppCurrentVersion;
    }

    /**
     * 获取当前版本 - 大版本
     *
     * @return
     */
    public static String getAppMaxVersion() {
        if (mAppMaxVersion == null)
            getAppCurrentVersion();
        return mAppMaxVersion;
    }

    /**
     * 获取当前版本 - 小版本
     *
     * @return
     */
    public static String getAppMinVersion() {
        if (mAppMinVersion == null)
            getAppCurrentVersion();
        return mAppMinVersion;
    }

    public static String getUniqueId() {
        String androidID = Settings.Secure.getString(BaseApplication.instance.getContentResolver(), Settings.Secure.ANDROID_ID);
        String id = androidID + Build.SERIAL;
        try {
            return toMD5(id);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return id;
        }
    }

    private static String toMD5(String text) throws NoSuchAlgorithmException {
        //获取摘要器 MessageDigest
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        //通过摘要器对字符串的二进制字节数组进行hash计算
        byte[] digest = messageDigest.digest(text.getBytes());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < digest.length; i++) {
            //循环每个字符 将计算结果转化为正整数;
            int digestInt = digest[i] & 0xff;
            //将10进制转化为较短的16进制
            String hexString = Integer.toHexString(digestInt);
            //转化结果如果是个位数会省略0,因此判断并补0
            if (hexString.length() < 2) {
                sb.append(0);
            }
            //将循环结果添加到缓冲区
            sb.append(hexString);
        }
        //返回整个结果
        return sb.toString();
    }


    public static String listToString(List<String> strings) {
        String str = "";
        if (strings.size() > 0) {
            StringBuilder strBuilder = new StringBuilder();
            for (String letter : strings) {
                strBuilder.append(letter);
                strBuilder.append(",");
            }
            str = strBuilder.toString();
            str = str.substring(0, str.length() - ",".length());
            return str;
        }
        return "";
    }

    public static List<String> stringToList(String strings) {
        List<String> str = new ArrayList<>();
        if (!TextUtils.isEmpty(strings)) {
            StringBuilder strBuilder = new StringBuilder();
            String[] split = strings.split(",");
            str.addAll(Arrays.asList(split));
            return str;
        }
        return str;
    }
    public static List<String> stringToListByXG(String strings) {
        List<String> str = new ArrayList<>();
        if (!TextUtils.isEmpty(strings)) {
            StringBuilder strBuilder = new StringBuilder();
            String[] split = strings.split("/");
            str.addAll(Arrays.asList(split));
            return str;
        }
        return str;
    }
}
