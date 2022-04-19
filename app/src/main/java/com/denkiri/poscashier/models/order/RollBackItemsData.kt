package com.denkiri.poscashier.models.order
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RollBackItemsData {
    @SerializedName("error")
    @Expose
    var error = false
    @SerializedName("status")
    @Expose
    var status = 0
    @SerializedName("orders")
    @Expose
    var order: List<RollBackItems>? = null
    @SerializedName("totalAmount")
    @Expose
    var totalAmount: TotalAmountB? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}