package com.denkiri.poscashier.models.order
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("transactionId"))],
        primaryKeys = ["transactionId"]
)
class Order {
    @field:SerializedName("transaction_id")
    @Expose
    var transactionId : Int?= 0

    @field:SerializedName("invoice")
    @Expose
    var invoice: String? = null

    @field:SerializedName("product")
    @Expose
    var product: String? = null

    @field:SerializedName("qty")
    @Expose
    var qty: String? = null

    @field:SerializedName("amount")
    @Expose
    var amount: Double= 0.00

    @field:SerializedName("name")
    @Expose
    var name: String? = null

    @field:SerializedName("price")
    @Expose
    var price: Double= 0.00

    @field:SerializedName("discount")
    @Expose
    var discount: Double= 0.00

    @field:SerializedName("category")
    @Expose
    var category: String? = null

    @field:SerializedName("date")
    @Expose
    var date: String? = null

    @field:SerializedName("omonth")
    @Expose
    var omonth: String? = null

    @field:SerializedName("oyear")
    @Expose
    var oyear: String? = null

    @field:SerializedName("qtyleft")
    @Expose
    var qtyleft: String? = null

    @field:SerializedName("dname")
    @Expose
    var dname: String? = null

    @field:SerializedName("vat")
    @Expose
    var vat:Double= 0.00

    @field:SerializedName("expiry")
    @Expose
    var expiry: String? = null

    @field:SerializedName("total_amount")
    @Expose
    var totalAmount: Double= 0.00

    @field:SerializedName("total_cost")
    @Expose
    var totalCost: Double= 0.00
    @field:SerializedName("vat_value")
    @Expose
    var vatValue: Double= 0.00
    @field:SerializedName("commission_value")
    @Expose
    var commissionValue: Double= 0.00
    @field:SerializedName("discount_value")
    @Expose
    var discountValue: Double= 0.00
    @field:SerializedName("cost_value")
    @Expose
    var costValue: Double= 0.00
    @field:SerializedName("product_id")
    @Expose
    var productId: Double= 0.00
}