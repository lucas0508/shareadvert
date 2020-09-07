package com.qujiali.shareadvert.common.utils


object Constant {
    const val BASE_URL = "https://www.xiangzq.cn"

    //http://192.168.1.142


    const val BASE_DEBUG_URL = "http://192.168.1.142/";

    //const val BASE_DEBUG_URL = "http://192.168.1.58:8076";

    /**
     * 用户协议
     */
    const val AGREEMENT_URL = BASE_URL+"shareh5/static/guanggaoquan/xieyi/ggqyhfwxy.html"


    /**
     * 隐私政策
     */
    const val PRIVACYPOLICY_URL = BASE_URL+"shareh5/static/guanggaoquan/xieyi/ggqyhyszc.html"


    /*----------------------------------------------------*/
    const val SP_IS_FIRST = "isFirst"
    const val SP_APP_TOKEN = "TOKEN"
    const val SP_APP_CITY_CODE = "CITY_CODE"

    const val LOGIN_KEY = "login"
    const val KEY_COMPANYID = "companyId"
    const val KEY_USER_ROLE = "userRole"

    const val KEY_RESOURCESID = "resourcesId"
    const val KEY_DEMANDID = "demandId"

    const val KEY_USERNAME = "userName"
    const val KEY_IMGPATH = "imgPath"




/*----------------------------------------------------*/

    const val SAVE_USER_LOGIN_KEY = "user/login"
    const val SAVE_USER_REGISTER_KEY = "user/register"
    const val SET_COOKIE_KEY = "set-cookie"
    const val COOKIE_NAME = "Cookie"

    const val NIGHT_MODE = "night_mode"

    const val HOME = 0
    const val RESOURCES = 1
    const val REQUIRE = 2
    const val MINE = 3

    const val SUCCESS = 200
    const val NOT_LOGIN = 401

    const val ADD_TODO = 1.toString() + ""

    const val EDIT_TODO = 2.toString() + ""

    const val TODO_WORK = 1

    const val TODO_STUDY = 2

    const val KEY_TODO_TITLE = "todo_title"

    const val KEY_TODO_CONTENT = "todo_content"

    const val KEY_TODO_DATE = "todo_date"


    const val KEY_TODO_PRIORITY = "todo_priority"

    const val KEY_TODO_ID = "todo_id"

    const val KEY_TODO_TYPE = "todo_type"

    const val KEY_TODO_HANDLE_TYPE = "todo_handle"

    // 二维码扫描
    const val REQUEST_CODE_SCAN = 1

    const val CODED_CONTENT = "codedContent"

    const val INTENT_ZXING_CONFIG = "zxingConfig"

    // 应用更新
    const val UPDATEAPK = "update_apk"
}