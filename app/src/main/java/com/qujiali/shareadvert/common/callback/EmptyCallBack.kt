package com.qujiali.shareadvert.common.callback

import com.kingja.loadsir.callback.Callback
import com.qujiali.shareadvert.R

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/25
 * Time: 19:26
 */

class EmptyCallBack : Callback() {
    override fun onCreateView(): Int = R.layout.layout_empty
}