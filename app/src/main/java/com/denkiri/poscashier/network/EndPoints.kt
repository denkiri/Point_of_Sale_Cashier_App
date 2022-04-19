package com.denkiri.poscashier.network
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.cashier.UsersData
import com.denkiri.poscashier.models.customer.CustomerData
import com.denkiri.poscashier.models.expense.ExpenseData
import com.denkiri.poscashier.models.expense.IncomeData
import com.denkiri.poscashier.models.oauth.Oauth
import com.denkiri.poscashier.models.order.CartData
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.order.PendingOrdersData
import com.denkiri.poscashier.models.order.RollBackItemsData
import com.denkiri.poscashier.models.pending.PendingTransactionData
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.models.report.SalesReportData
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface EndPoints {
    @FormUrlEncoded
    @POST("cashier/signin.php")
    fun signIn(@Field("username") username: String?, @Field("password") password: String?): Call<Oauth>
    @GET("cashier/Products.php")
    fun products(): Call<ProductData>
    @GET("cashier/order.php")
    fun order(): Call<OrderData>
    @FormUrlEncoded
    @POST("cashier/incoming.php")
    fun addOrder(@Field("invoice") invoice: String?, @Field("productcode") productCode: String?, @Field("qty") quantity: String?, @Field("commision") commision: String?,@Field("vat") vat: String?,@Field("discount") discount: String?,@Field("price") price: String?,@Field("cost") cost: String?,@Field("expirationdate") expirationDate: String?,@Field("productname") productName: String?,@Field("descriptionname") descriptionName: String?,@Field("category") category: String?,@Field("qtyleft") quantityLeft: String?,@Field("productid") pid: String?,@Field("brand") brand: String?): Call<OrderData>
    @FormUrlEncoded
    @POST("cashier/order.php")
    fun getOrder(@Field("invoice") invoice: String?): Call<CartData>
    @FormUrlEncoded
    @POST("cashier/increaseCartItem.php")
    fun increaseCartItem(@Field("invoice") invoice: String?, @Field("productcode") productCode: String?, @Field("qty") quantity: String?, @Field("commision") commision: String?,@Field("vat") vat: String?,@Field("discount") discount: String?,@Field("price") price: String?,@Field("cost") cost: String?,@Field("expirationdate") expirationDate: String?,@Field("productname") productName: String?,@Field("descriptionname") descriptionName: String?,@Field("category") category: String?,@Field("qtyleft") quantityLeft: String?,@Field("productid") pid: String?): Call<OrderData>
    @FormUrlEncoded
    @POST("cashier/decreaseCartItem.php")
    fun decreaseCartItem(@Field("invoice") invoice: String?, @Field("productcode") productCode: String?, @Field("qty") quantity: String?, @Field("commision") commision: String?,@Field("vat") vat: String?,@Field("discount") discount: String?,@Field("price") price: String?,@Field("cost") cost: String?,@Field("expirationdate") expirationDate: String?,@Field("productname") productName: String?,@Field("descriptionname") descriptionName: String?,@Field("category") category: String?,@Field("qtyleft") quantityLeft: String?,@Field("productid") pid: String?): Call<OrderData>
    @FormUrlEncoded
    @POST("cashier/deleteCartItem.php")
    fun deleteCartItem(@Field("transactionid") transactionId: String?, @Field("quantity") quantity: String?, @Field("productcode") productCode: String?, @Field("invoice") invoice: String?): Call<OrderData>
    @FormUrlEncoded
    @POST("cashier/credit.php")
    fun saveCreditSale(@Field("invoice") invoice: String?, @Field("cashier") cashier: String?, @Field("ptype") paymentType: String?, @Field("amount") amount: String?,@Field("due") dueDate: String?,@Field("p_amount") pAmount: String?,@Field("cname") customerName: String?): Call<TransactionData>
    @FormUrlEncoded
    @POST("cashier/cash.php")
    fun saveCashSale(@Field("invoice") invoice: String?, @Field("cashier") cashier: String?, @Field("ptype") paymentType: String?, @Field("amount") amount: String?,@Field("due") dueDate: String?,@Field("p_amount") pAmount: String?,@Field("cname") customerName: String?): Call<TransactionData>
    @POST("payment/pay.php")
    @FormUrlEncoded
    fun saveMpesaSale(@Field("invoice") invoice: String?, @Field("cashier") cashier: String?, @Field("ptype") paymentType: String?, @Field("amount") amount: String?,@Field("cname") customerName: String?,@Field("phone") phone: String?): Call<TransactionData>
    @GET("cashier/activeCustomers.php")
    fun customers(): Call<CustomerData>
    @GET("payment/pendingPayments.php")
    fun pendingTransactions(): Call<PendingTransactionData>
    @GET("cashier/rollBackProducts.php")
    fun pendingOrders(): Call<PendingOrdersData>
    @POST("payment/payPending.php")
    @FormUrlEncoded
    fun savePendingMpesaSale(@Field("amount") amount: String?,@Field("phone") phone: String?,@Field("orderId") invoice: String?): Call<TransactionData>
    @GET("cashier/salesreport.php")
    fun salesReport():Call<SalesReportData>
    @FormUrlEncoded
    @POST("cashier/order.php")
    fun rollBackItems(@Field("invoice") invoice: String?): Call<RollBackItemsData>
    @FormUrlEncoded
    @POST("cashier/deleteSale.php")
    fun deleteInvoiceItems(@Field("id") transactionId: String?,@Field("invoice") invoice: String?,@Field("qty") quantity: String?,@Field("code") productCode: String?,@Field("total") total: String?,@Field("ptype") paymentType: String?): Call<RollBackItemsData>
    @FormUrlEncoded
    @POST("subscription/subscription.php")
    fun checkSubscription(@Field("code") code: String?): Call<PaymentResponseData>
    @FormUrlEncoded
    @POST("cashier/recordIncome.php")
    fun recordIncome(@Field("service")expense:String?,@Field("amount")amount: String?,@Field("month")month: String?,@Field("date")date: String?,@Field("year")year: String?,@Field("cashier")cashier: String?,@Field("receipt")items: String?): Call<IncomeData>
    @GET("cashier/activeExpenses.php")
    fun activeExpenses(): Call<ExpenseData>
    @GET("cashier/users.php")
    fun users(): Call<UsersData>
    @FormUrlEncoded
    @POST("cashier/addExpense.php")
    fun addExpense(@Field("name")name:String?,@Field("amount")amount: String?): Call<ExpenseData>

}