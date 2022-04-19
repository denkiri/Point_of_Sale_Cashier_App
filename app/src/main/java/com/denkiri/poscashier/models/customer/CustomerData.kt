package com.denkiri.poscashier.models.customer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CustomerData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("customers")
    @Expose
    var customers: List<Customer>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}