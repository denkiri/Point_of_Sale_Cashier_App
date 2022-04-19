package com.denkiri.poscashier.models.order

import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class TotalAmountB {
    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double = 0.0
    @Ignore
    constructor(totalAmount: Double) {
        this.totalAmount = totalAmount
    }
    constructor()
}