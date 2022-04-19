package com.denkiri.poscashier.models.payment
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CheckPaymentResponseData {
    @SerializedName("error")
    @Expose
    var error = false
    @SerializedName("status")
    @Expose
    var status = 0
    @SerializedName("success")
    @Expose
    var success = true
    @SerializedName("message")
    @Expose
    var message: String? = null
}