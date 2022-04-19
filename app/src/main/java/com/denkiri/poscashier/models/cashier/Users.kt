package com.denkiri.poscashier.models.cashier
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Users {
    @SerializedName("cashier_id")
    @Expose
    var cashierId = 0

    @SerializedName("cashier_name")
    @Expose
    var cashierName: String? = null

    @SerializedName("position")
    @Expose
    var position: String? = null

    @SerializedName("username")
    @Expose
    var username: String? = null

    @SerializedName("password")
    @Expose
    var password: String? = null
}