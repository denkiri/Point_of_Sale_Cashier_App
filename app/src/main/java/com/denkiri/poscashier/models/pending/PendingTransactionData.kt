package com.denkiri.poscashier.models.pending

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class PendingTransactionData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("success")
    @Expose
    var success = false

    @SerializedName("orders")
    @Expose
    var orders: List<PendingTransaction>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null

}