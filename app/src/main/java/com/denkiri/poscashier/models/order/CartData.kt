package com.denkiri.poscashier.models.order

import com.denkiri.poscashier.models.product.Product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CartData {
    @SerializedName("error")
    @Expose
    var error = false
    @SerializedName("status")
    @Expose
    var status = 0
    @SerializedName("orders")
    @Expose
    var order: List<Order>? = null
    @SerializedName("totalAmount")
    @Expose
    var totalAmount: TotalAmount? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}