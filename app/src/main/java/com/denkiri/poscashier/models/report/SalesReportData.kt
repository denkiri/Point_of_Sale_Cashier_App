package com.denkiri.poscashier.models.report

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class SalesReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("salesreport")
    @Expose
    var salesreport: List<SalesReport>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}