package com.denkiri.poscashier.models
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class TransactionData {
    @SerializedName("error")
    @Expose
    var error:Boolean? = null

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("success")
    @Expose
    var success :Boolean? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}