package com.denkiri.poscashier.models.oauth
import androidx.room.Ignore
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Oauth {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("profile")
    @Expose
    var profile: Profile? = null
    @SerializedName("token")
    @Expose
    var token: String? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
    @Ignore
    constructor(profile: Profile?) {
        this.profile = profile
    }
    constructor()
}