package com.denkiri.poscashier.models.order
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PendingOrdersData {
    @SerializedName("error")
    @Expose
    var error = false
    @SerializedName("status")
    @Expose
    var status = 0
    @SerializedName("orders")
    @Expose
    var orders: List<PendingOrders>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}