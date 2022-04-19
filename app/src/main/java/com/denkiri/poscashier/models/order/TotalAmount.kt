package com.denkiri.poscashier.models.order

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("totalAmount"))],
        primaryKeys = ["totalAmount"]
)
class TotalAmount {
    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double = 0.0
    @Ignore
    constructor(totalAmount: Double) {
        this.totalAmount = totalAmount
    }
    constructor()
}