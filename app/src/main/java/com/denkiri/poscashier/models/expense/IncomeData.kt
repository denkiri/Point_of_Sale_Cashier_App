package com.denkiri.poscashier.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class IncomeData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("totalIncome")
    @Expose
    var totalIncome: TotalIncome? = null
    @SerializedName("success")
    @Expose
    var success: Boolean? = null
    @SerializedName("income")
    @Expose
    var income: List<Income>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}