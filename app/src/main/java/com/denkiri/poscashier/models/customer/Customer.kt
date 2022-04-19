package com.denkiri.poscashier.models.customer
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Customer {
    @SerializedName("customer_id")
    @Expose
    var customerId = 0

    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("contact")
    @Expose
    var contact: String? = null

    @SerializedName("membership_number")
    @Expose
    var membershipNumber: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
}