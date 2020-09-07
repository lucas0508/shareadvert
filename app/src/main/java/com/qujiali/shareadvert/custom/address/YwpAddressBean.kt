package com.qujiali.shareadvert.custom.address

import java.io.Serializable

class YwpAddressBean : Serializable {
    var province: List<AddressItemBean>? = null
    var city: List<AddressItemBean>? = null
    var district: List<AddressItemBean>? = null

    class AddressItemBean : Serializable {
        var i: String? = null
        var n: String? = null
        var p: String? = null

    }
}