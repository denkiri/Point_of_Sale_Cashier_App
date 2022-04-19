package com.denkiri.poscashier.models.product
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("productId"))],
        primaryKeys = ["productId"]
)
class Product {
    @field:SerializedName("product_id")
    @Expose
    var productId: Int? = 0

    @field:SerializedName("product_code")
    @Expose
    var productCode: String? = null

    @field:SerializedName("product_name")
    @Expose
    var productName: String? = null

    @field:SerializedName("description_name")
    @Expose
    var descriptionName: String? = null

    @field:SerializedName("unit")
    @Expose
    var unit: String? = null

    @field:SerializedName("cost")
    @Expose
    var cost: String? = null

    @field:SerializedName("price")
    @Expose
    var price: String?= null
    @SerializedName("vat")
    @Expose
    var vat: String?= null
    @field:SerializedName("supplier")
    @Expose
    var supplier: String? = null

    @field:SerializedName("qty_left")
    @Expose
    var qtyLeft:Int? = 0

    @field:SerializedName("category")
    @Expose
    var category: String? = null
    @field:SerializedName("brand")
    @Expose
    var brand: String? = null
    @field:SerializedName("date_delivered")
    @Expose
    var dateDelivered: String? = null

    @field:SerializedName("expiration_date")
    @Expose
    var expirationDate: String? = null

    @field:SerializedName("reorder")
    @Expose
    var reorder: Int? = null

    @field:SerializedName("suplier_id")
    @Expose
    var suplierId: Int? = 0

    @field:SerializedName("suplier_name")
    @Expose
    var suplierName: String? = null

    @field:SerializedName("suplier_address")
    @Expose
    var suplierAddress: String? = null

    @field:SerializedName("suplier_contact")
    @Expose
    var suplierContact: String? = null

    @field:SerializedName("contact_person")
    @Expose
    var contactPerson: String? = null
}