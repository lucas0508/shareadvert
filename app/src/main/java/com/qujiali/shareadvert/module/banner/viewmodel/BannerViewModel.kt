package com.qujiali.shareadvert.module.banner.viewmodel

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import androidx.lifecycle.MutableLiveData
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.base.viewmodel.BaseViewModel
import com.qujiali.shareadvert.common.utils.GlideImageLoader
import com.qujiali.shareadvert.module.banner.model.BannerResponse
import com.qujiali.shareadvert.module.banner.repository.BannerRepository
import com.qujiali.shareadvert.network.initiateRequest

class BannerViewModel :BaseViewModel<BannerRepository>(){

    // 使用协程 + Retrofit2.6以上版本
    val onQueryBanner: MutableLiveData<String> = MutableLiveData()


    fun onQueryBanner(cityCode: String, type: String) {
        initiateRequest({onQueryBanner.value = mRepository.onQueryBanner(cityCode, type)}, loadState)
    }

  /*  @BindingAdapter("pictureUrl")
    fun loadImage(view: ImageView, imageUrl: String) {
        GlideImageLoader().displayImage(BaseApplication.instance,imageUrl,view)
    }*/

}