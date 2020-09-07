package com.qujiali.shareadvert.module.resources.view

import android.text.TextUtils
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse

open class MineResourcesAdapter(layoutId: Int, listData: MutableList<ResourcesResponse>?) :
    BaseQuickAdapter<ResourcesResponse, BaseViewHolder>(layoutId, listData) {

    override fun convert(viewHolder: BaseViewHolder, article: ResourcesResponse?) {
        viewHolder.let { holder ->
            article?.let {
                Logger.d("item：(${it})")
                holder.setText(R.id.home_title, it.title)
                    .setText(R.id.home_time, it.createTime)
                    .setText(R.id.home_content, it.info)
                    .setText(R.id.home_city, it.areaList.joinToString(""))
                    .setText(R.id.home_call, it.callTimes.toString())
                    .setText(R.id.home_review, it.viewTimes.toString())
                    .setText(R.id.tvStatus, holderStatus(it))
                    .setBackgroundRes(R.id.tvStatus,holderStatusColor(it))
                Glide.with(mContext)
                    .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                    .load(handleImage(it)).into(holder.getView(R.id.hone_image))

            }
        }
    }

    private fun holderStatus(it: ResourcesResponse): String {
        when (it.status) {
            0 ->   return "审核中"
            1 -> if (it.state == 1) return "审核通过" else if (it.state == 0) return "已下线"
            2 -> return "审核拒绝"
        }
        return ""
    }


    private fun holderStatusColor(it: ResourcesResponse): Int {
        when (it.status) {
            0 -> return   R.drawable.common_button_selector_red
            1 ->  if (it.state == 1) return R.drawable.common_button_selector_green else if (it.state == 0) return R.drawable.common_button_selector_gray
            2 -> return  R.drawable.common_button_selector_reditem
        }
       return 0
    }

    private fun isCollect(it: ResourcesResponse): Boolean =
        !(TextUtils.isEmpty(it.userRole) || "NONE" == it.userRole)


    private fun handleImage(it: ResourcesResponse): String {
        it.let {
            if (!TextUtils.isEmpty(it.img))
                return it.img.split(",")[0]
            return ""
        }
    }
}