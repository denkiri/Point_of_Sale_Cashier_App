package com.denkiri.poscashier.models.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SalesReport {
    @SerializedName("transaction_id")
    @Expose
    var transactionId:Int? = 0

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("cashier")
    @Expose
    var cashier: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Double? = 0.0

    @SerializedName("due_date")
    @Expose
    var dueDate: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("balance")
    @Expose
    var balance: Double? = 0.0

    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double? = 0.0

    @SerializedName("cash")
    @Expose
    var cash: String? = null

    @SerializedName("month")
    @Expose
    var month: String? = null

    @SerializedName("year")
    @Expose
    var year: String? = null

    @SerializedName("p_amount")
    @Expose
    var pAmount: Double? = 0.0

    @SerializedName("vat")
    @Expose
    var vat: String? = null

    @SerializedName("time")
    @Expose
    var time: String? = null
}