package com.qujiali.shareadvert.module.settlein.model

import android.os.Parcel
import android.os.Parcelable
import com.contrarywind.interfaces.IPickerViewData
import kotlinx.android.parcel.Parcelize
import java.io.Serializable
import java.util.*

@Parcelize
class AdressDataEntity_oldddd : IPickerViewData, Parcelable {
    var rows: MutableList<RowsBean?>? =
        null

    override fun getPickerViewText(): String? {
        return null
    }
    @Parcelize
    class RowsBean : Parcelable {
        var enable: String? = null
        var adCode: String? = null
        var name: String? = null
        var child: List<ChildBeanXX>? =
            null
        @Parcelize
        class ChildBeanXX : Parcelable {
            /**
             * enable : 2
             * adCode : 110000
             * name : 北京市
             * child : [{"enable":"3","adCode":"110100","name":"北京市","child":[{"enable":"4","adCode":"110101","name":"东城区"},{"enable":"4","adCode":"110102","name":"西城区"},{"enable":"4","adCode":"110105","name":"朝阳区"},{"enable":"4","adCode":"110106","name":"丰台区"},{"enable":"4","adCode":"110107","name":"石景山区"},{"enable":"4","adCode":"110108","name":"海淀区"},{"enable":"4","adCode":"110109","name":"门头沟区"},{"enable":"4","adCode":"110111","name":"房山区"},{"enable":"4","adCode":"110112","name":"通州区"},{"enable":"4","adCode":"110113","name":"顺义区"},{"enable":"4","adCode":"110114","name":"昌平区"},{"enable":"4","adCode":"110115","name":"大兴区"},{"enable":"4","adCode":"110116","name":"怀柔区"},{"enable":"4","adCode":"110117","name":"平谷区"},{"enable":"4","adCode":"110118","name":"密云区"},{"enable":"4","adCode":"110119","name":"延庆区"}]}]
             */
            var enable: String? = null
            var adCode: String? = null
            var name: String? = null
            var child: List<ChildBeanX>? =
                null
            @Parcelize
            class ChildBeanX : Parcelable {
                /**
                 * enable : 3
                 * adCode : 110100
                 * name : 北京市
                 * child : [{"enable":"4","adCode":"110101","name":"东城区"},{"enable":"4","adCode":"110102","name":"西城区"},{"enable":"4","adCode":"110105","name":"朝阳区"},{"enable":"4","adCode":"110106","name":"丰台区"},{"enable":"4","adCode":"110107","name":"石景山区"},{"enable":"4","adCode":"110108","name":"海淀区"},{"enable":"4","adCode":"110109","name":"门头沟区"},{"enable":"4","adCode":"110111","name":"房山区"},{"enable":"4","adCode":"110112","name":"通州区"},{"enable":"4","adCode":"110113","name":"顺义区"},{"enable":"4","adCode":"110114","name":"昌平区"},{"enable":"4","adCode":"110115","name":"大兴区"},{"enable":"4","adCode":"110116","name":"怀柔区"},{"enable":"4","adCode":"110117","name":"平谷区"},{"enable":"4","adCode":"110118","name":"密云区"},{"enable":"4","adCode":"110119","name":"延庆区"}]
                 */
                var enable: String? = null
                var adCode: String? = null
                var name: String? = null
                var child: List<ChildBean>? =
                    null
                @Parcelize
                class ChildBean : Parcelable{
                    /**
                     * enable : 4
                     * adCode : 110101
                     * name : 东城区
                     */
                    var enable: String? = null
                    var adCode: String? = null
                    var name: String? = null
                    var addresscode: String? = null
                    var isIscheck = false
                        private set

                    fun setIscheck(ischeck: Boolean) {
                        isIscheck = ischeck
                    }
                }
            }
        }
    }


}








