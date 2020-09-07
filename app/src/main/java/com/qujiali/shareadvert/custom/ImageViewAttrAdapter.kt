package com.qujiali.shareadvert.custom

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.qujiali.shareadvert.base.view.BaseApplication


object  ImageViewAttrAdapter {
    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, bitmap: Bitmap?) {
        view.setImageBitmap(bitmap)
    }

    @BindingAdapter("android:src")
    fun setSrc(view: ImageView, resId: Int) {
        view.setImageResource(resId)
    }

    @BindingAdapter("pictureUrl")
    @JvmStatic
    fun loadImage(
        imageView: ImageView,
        url: String?
    ) {
        Glide.with(BaseApplication.instance)
            .load(url)
            .into(imageView)
    }
}