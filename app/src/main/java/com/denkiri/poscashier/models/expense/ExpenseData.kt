package com.denkiri.poscashier.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ExpenseData {
    @SerializedName("error")
    @Expose
     var error: Boolean? = null
    @SerializedName("status")
    @Expose
     var status: Int? = null
    @SerializedName("expenses")
    @Expose
     var expenses: List<Expenses>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}