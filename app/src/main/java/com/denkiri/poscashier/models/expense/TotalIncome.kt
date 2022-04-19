package com.denkiri.poscashier.models.expense
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
    indices = [(Index("totalIncome"))],
    primaryKeys = ["totalIncome"]
)
class TotalIncome {
    @SerializedName("total_income")
    @Expose
    var totalIncome: Double = 0.0
    @Ignore
    constructor(totalIncome: Double){
        this.totalIncome =totalIncome
    }
    constructor()
}