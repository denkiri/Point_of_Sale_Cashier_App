package com.denkiri.pharmacy.models.payment
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PaymentResponseData {
    @SerializedName("error")
    @Expose
    var error: Boolean?  = false
    @SerializedName("status")
    @Expose
    var status:Int? = 0
    @SerializedName("success")
    @Expose
    var success = true
    @SerializedName("message")
    @Expose
    var message: String? = null
}