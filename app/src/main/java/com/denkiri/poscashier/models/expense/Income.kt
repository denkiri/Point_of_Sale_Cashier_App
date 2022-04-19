package com.denkiri.poscashier.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Income {
    @SerializedName("service_id")
    @Expose
    var serviceId: Int? =0
    @SerializedName("service")
    @Expose
    var service: String? = null
    @SerializedName("amount")
    @Expose
    var amount: Double? = 0.00
    @SerializedName("month")
    @Expose
    var month: String? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("year")
    @Expose
    var year: String? = null
    @SerializedName("service_cashier")
    @Expose
    var serviceCashier: String? = null
    @SerializedName("receipt_number")
    @Expose
    var receiptNumber: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("cashier_id")
    @Expose
    var cashierID: Int? = 0
    @SerializedName("cashier_name")
    @Expose
    var cashierName: String? = null


}