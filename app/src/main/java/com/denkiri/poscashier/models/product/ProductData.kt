package com.denkiri.poscashier.models.product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ProductData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("products")
    @Expose
    var products: List<Product>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}