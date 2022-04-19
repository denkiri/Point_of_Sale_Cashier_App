package com.denkiri.poscashier.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Expenses {
    @SerializedName("id")
    @Expose
     var id: Int? =0
    @SerializedName("name")
    @Expose
     var name: String? = null
    @SerializedName("total_amount")
    @Expose
     var amount: Double? =0.00
    @SerializedName("status")
    @Expose
    var status: Int? = null
}