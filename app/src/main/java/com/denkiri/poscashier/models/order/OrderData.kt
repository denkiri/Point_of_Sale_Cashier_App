package com.denkiri.poscashier.models.order
import com.denkiri.poscashier.models.product.Product
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class OrderData {
    @SerializedName("error")
    @Expose
   var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("success")
    @Expose
    var success = false
    @SerializedName("totalAmount")
    @Expose
    var totalAmount: TotalAmount? = null
    @SerializedName("orders")
    @Expose
    var order: List<Order>? = null
    @SerializedName("products")
    @Expose
    var products: List<Product>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}