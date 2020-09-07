package com.qujiali.shareadvert.module.resources.view

import android.os.Build
import android.text.Html
import android.text.TextUtils
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/25
 * Time: 21:09
 */
open class ArticleAdapter(layoutId: Int, listData: MutableList<ResourcesResponse>?) :
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
                    .setVisible(R.id.home_icon, isCollect(it))
                Glide.with(mContext)
                    .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                    .load(handleImage(it)).into(holder.getView(R.id.hone_image))

                if (!TextUtils.isEmpty(it.keyword)) {
                    val split1 = it.keyword.split(",")
                    when (split1.size) {
                        1 -> {
                            holder.setText(R.id.home_type1, split1[0])
                            holder.setVisible(R.id.home_type1, true)
                            holder.setVisible(R.id.home_type2, false)
                            holder.setVisible(R.id.home_type3, false)
                        }
                        2 -> {
                            holder.setText(R.id.home_type1, split1[0])
                            holder.setText(R.id.home_type2, split1[1])
                            holder.setVisible(R.id.home_type1, true)
                            holder.setVisible(R.id.home_type2, true)
                            holder.setVisible(R.id.home_type3, false)
                        }
                        3 -> {
                            holder.setText(R.id.home_type1, split1[0])
                            holder.setText(R.id.home_type2, split1[1])
                            holder.setText(R.id.home_type2, split1[2])
                            holder.setVisible(R.id.home_type1, true)
                            holder.setVisible(R.id.home_type2, true)
                            holder.setVisible(R.id.home_type3, true)
                        }
                        else -> {
                        }
                    }
                }
            }
        }
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