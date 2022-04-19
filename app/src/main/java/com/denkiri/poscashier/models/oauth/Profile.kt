package com.denkiri.poscashier.models.oauth
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("cashierId"))],
        primaryKeys = ["cashierId"]
)
class Profile {
    @field:SerializedName("cashier_id")
    var cashierId : Int? = 0
    @field:SerializedName("cashier_name")
    var cashierName: String? = null
    @field:SerializedName("position")
    var position: String? = null
    @field:SerializedName("username")
    var username: String? = null
    @field:SerializedName("password")
    var password: String? = null
    @field:SerializedName("token")
    var token: String? = null
    @field:SerializedName("business_code")
    var businessCode: String? = null

    @Ignore
    constructor(username: String?, password: String?) {
        this.username = username
        this.password = password
    }
    constructor()
}