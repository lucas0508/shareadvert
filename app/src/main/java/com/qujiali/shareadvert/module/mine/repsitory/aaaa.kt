package com.qujiali.shareadvert.module.mine.repsitory

import android.app.Activity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.qujiali.shareadvert.common.utils.MyAppGlideModule

class aaaa : Activity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val textView = ImageView(this)
        Glide.with(this).applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
            .load("").into(textView)
    }
}