package com.qujiali.shareadvert.common.callback

import com.kingja.loadsir.callback.Callback
import com.qujiali.shareadvert.R

/**
 * Created with Android Studio.
 * Description:
 * @author: 王拣贤
 * @date: 2020/02/22
 * Time: 14:29
 */
class ErrorCallBack : Callback() {
    override fun onCreateView(): Int = R.layout.layout_error
}