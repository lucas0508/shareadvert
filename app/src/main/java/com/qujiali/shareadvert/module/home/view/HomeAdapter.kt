package com.qujiali.shareadvert.module.home.view

import android.os.Build
import android.text.Html
import android.text.TextUtils
import android.view.View
import androidx.annotation.RequiresApi
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.orhanobut.logger.Logger
import com.qujiali.shareadvert.R
import com.qujiali.shareadvert.base.view.BaseApplication
import com.qujiali.shareadvert.common.utils.MyAppGlideModule
import com.qujiali.shareadvert.module.home.model.CompanyDataResponse
import com.qujiali.shareadvert.module.resources.model.ResourcesResponse
import kotlinx.android.synthetic.main.activity_settlein_company.*

/**
 * Created with Android Studio.
 * Description:
 * @author: Wangjianxian
 * @date: 2020/02/25
 * Time: 21:09
 */
open class HomeAdapter(layoutId: Int, listData: MutableList<CompanyDataResponse>?) :
    BaseQuickAdapter<CompanyDataResponse, BaseViewHolder>(layoutId, listData) {

    override fun convert(viewHolder: BaseViewHolder, article: CompanyDataResponse?) {
        viewHolder.let { holder ->
            article?.let {
                Logger.d("item：(${it})")


                if (!TextUtils.isEmpty(it.businessLicenseImg)) {
                    val split = it.businessLicenseImg.split(",")
                    Glide.with(BaseApplication.instance)
                        .applyDefaultRequestOptions(MyAppGlideModule.getRequestOptions())
                        .load(split[0])
                        .into(
                            holder.getView(R.id.hone_image)
                        )
                }else{
                    holder.setImageResource(R.id.hone_image,R.mipmap.ic_launcher)
                }


                val split1 = it.mainBusiness.split(",")
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
                }
                holder.setText(R.id.home_title, it.abbreviation)
                    .setText(R.id.home_time, it.createTime)
                    .setText(R.id.home_content, it.info)
                    .setText(R.id.home_city, it.areaList.joinToString(""))
                    .setText(R.id.home_review, it.review)

            }
        }
    }
}

