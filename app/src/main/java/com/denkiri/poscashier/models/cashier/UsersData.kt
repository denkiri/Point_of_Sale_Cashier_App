package com.denkiri.poscashier.models.cashier
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class UsersData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("cashier")
    @Expose
    var cashier: List<Users>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}