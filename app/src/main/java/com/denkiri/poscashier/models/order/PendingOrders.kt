package com.denkiri.poscashier.models.order
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PendingOrders {
    @SerializedName("transaction_id")
    @Expose
    var transactionId : Int?= 0

    @SerializedName("invoice")
    @Expose
    var invoice: String? = null

    @SerializedName("product")
    @Expose
    var product: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Double= 0.00

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("price")
    @Expose
    var price: Double= 0.00

    @SerializedName("discount")
    @Expose
    var discount: Double= 0.00

    @SerializedName("category")
    @Expose
    var category: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("omonth")
    @Expose
    var omonth: String? = null

    @SerializedName("oyear")
    @Expose
    var oyear: String? = null

    @SerializedName("qtyleft")
    @Expose
    var qtyleft: String? = null

    @SerializedName("dname")
    @Expose
    var dname: String? = null

    @SerializedName("vat")
    @Expose
    var vat:Double= 0.00

    @SerializedName("expiry")
    @Expose
    var expiry: String? = null

    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double= 0.00

    @SerializedName("total_cost")
    @Expose
    var totalCost: Double= 0.00
    @SerializedName("vat_value")
    @Expose
    var vatValue: Double= 0.00
    @SerializedName("commission_value")
    @Expose
    var commissionValue: Double= 0.00
    @SerializedName("discount_value")
    @Expose
    var discountValue: Double= 0.00
    @SerializedName("cost_value")
    @Expose
    var costValue: Double= 0.00
    @SerializedName("product_id")
    @Expose
    var productId: Double= 0.00
}