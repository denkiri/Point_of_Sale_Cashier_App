package com.denkiri.poscashier.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.customer.CustomerData
import com.denkiri.poscashier.models.oauth.Oauth
import com.denkiri.poscashier.models.oauth.Profile
import com.denkiri.poscashier.models.order.CartData
import com.denkiri.poscashier.models.order.Order
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.order.TotalAmount
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.storage.*
import kotlin.random.Random

class CartViewModel (application: Application) : AndroidViewModel(application){
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    internal var subscriptionActionRepository:SubscriptionActionRepository
    internal  var signInRepository: SignInRepository
    internal var customerRepository: CustomerRepository
    internal var productsRepository: ProductsRepository
    internal var orderRepository : OrderRepository
    internal var orderActionRepository: OrderActionRepository
    internal var transactionRepository:TransactionRepository
    internal var productsActionRepository: ProductActionRepository
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    private val customerObservable = MediatorLiveData<Resource<CustomerData>>()
    private val productActionObservable= MediatorLiveData<Resource<ProductData>>()
    private val orderActionObservable= MediatorLiveData<Resource<OrderData>>()
    private val transactionObservable=MediatorLiveData<Resource<TransactionData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    private val orderObservable = MediatorLiveData<Resource<CartData>>()
    private val offlineProductsObservable = MediatorLiveData<Resource<List<Product>>>()
    private val offlineOrdersObservable = MediatorLiveData<Resource<List<Order>>>()
    private  val subscriptionActionObservable = MediatorLiveData<Resource<PaymentResponseData>>()
    init {
        subscriptionActionRepository= SubscriptionActionRepository(application)
        productsActionRepository = ProductActionRepository(application)
        orderActionRepository= OrderActionRepository(application)
        productsRepository = ProductsRepository(application)
        orderRepository = OrderRepository(application)
        transactionRepository= TransactionRepository(application)
        customerRepository = CustomerRepository(application)
        signInRepository = SignInRepository(application)
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
        orderObservable.addSource(orderRepository.orderObservable) { data -> orderObservable.setValue(data) }
        transactionObservable.addSource(transactionRepository.transactionActionObservable){data ->transactionObservable.setValue(data)}
        offlineProductsObservable.addSource(productsRepository.offlineProductsObservable) { data -> offlineProductsObservable.setValue(data) }
        offlineOrdersObservable.addSource(productsRepository.offlineOrdersObservable) { data -> offlineOrdersObservable.setValue(data) }
        productActionObservable.addSource(productsActionRepository.productActionObservable) { data -> productActionObservable.setValue(data) }
        orderActionObservable.addSource(orderActionRepository.orderActionObservable) { data -> orderActionObservable.setValue(data) }
        customerObservable.addSource(customerRepository.customerObservable) { data -> customerObservable.setValue(data) }
        signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}
        subscriptionActionObservable.addSource(subscriptionActionRepository.subscriptionActionObservable){ data->   subscriptionActionObservable.setValue(data)}
    }
    fun getProducts() {
        productsRepository.getProducts()
    }
    fun getTotalAmount(): LiveData<TotalAmount> {
        return orderActionRepository.getTotalAmount()
    }
    fun observeCustomer(): LiveData<Resource<CustomerData>> {
        return customerObservable
    }
    fun checkSubscription(code:String){
        subscriptionActionRepository.subscription(code)
    }

    fun observeSubscriptionAction(): LiveData<Resource<PaymentResponseData>> {
        return subscriptionActionObservable
    }
    fun getCustomer() {
        customerRepository.getCustomers()
    }
    fun getTotal(): String {
        return preferenceManager.getTotalAmount()
    }
    fun getOrder(invoice:String?) {
        orderRepository.getOrder(invoice!!)
    }
    fun observeCartData(): LiveData<Resource<CartData>> {
        return orderObservable
    }
    fun observeTransactionData(): LiveData<Resource<TransactionData>> {
        return transactionObservable
    }
    fun saveInvoiceNumber(invoice:String){
        productsRepository.saveInvoiceNumber(invoice)
    }

    fun getRandPassword(n: Int): String
    {
        val characterSet = "003232303232023232023456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val random = Random(System.nanoTime())
        val password = StringBuilder()

        for (i in 0 until n) {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return productsObservable
    }
    fun addOrder(invoice:String?,productCode:String?,quantity:String?,commision:String?,vat:String?,discount:String?,price:String?,cost:String?,expirationDate:String?,productName:String?,descriptionName:String?,category: String?,quantityLeft: String?, pid: String?,brand:String?){
        orderActionRepository.addOrder(invoice!!, productCode!!,quantity!!,commision!!,vat!!,discount!!,price!!,cost!!,expirationDate!!,productName!!,descriptionName!!, category, quantityLeft, pid,brand)
    }
    fun increaseCartItem(invoice:String?,productCode:String?,quantity:String?,commision:String?,vat:String?,discount:String?,price:String?,cost:String?,expirationDate:String?,productName:String?,descriptionName:String?,category: String?,quantityLeft: String?, pid: String?){
        orderActionRepository.increaseCart(invoice!!, productCode!!,quantity!!,commision!!,vat!!,discount!!,price!!,cost!!,expirationDate!!,productName!!,descriptionName!!, category, quantityLeft, pid)
    }
    fun decreaseCartItem(invoice:String?,productCode:String?,quantity:String?,commision:String?,vat:String?,discount:String?,price:String?,cost:String?,expirationDate:String?,productName:String?,descriptionName:String?,category: String?,quantityLeft: String?, pid: String?){
        orderActionRepository.decreaseCart(invoice!!, productCode!!,quantity!!,commision!!,vat!!,discount!!,price!!,cost!!,expirationDate!!,productName!!,descriptionName!!, category, quantityLeft, pid)
    }
    fun deleteCartItem(transactionId: String?,quantity: String?,productCode: String?,invoice: String?){
        orderActionRepository.deleteCart(transactionId!!, quantity!!,productCode!!,invoice!!)
    }
    fun saveCreditSale(invoice: String?,cashier: String?,paymentType: String?,amount: String?,dueDate: String?,pAmount: String?,customerName: String?){
        transactionRepository.saveCreditSale(invoice!!, cashier!!, paymentType!!, amount!!, dueDate!!, pAmount!!, customerName!!)
    }
    fun saveCashSale(invoice: String?,cashier: String?,paymentType: String?,amount: String?,dueDate: String?,pAmount: String?,customerName: String?){
        transactionRepository.saveCashSale(invoice!!, cashier!!, paymentType!!, amount!!, dueDate!!, pAmount!!, customerName!!)
    }
    fun saveMpesaSale(invoice: String?,cashier: String?,paymentType: String?,amount: String?,customerName: String?,phone: String?){
        transactionRepository.saveMpesaSale(invoice!!, cashier!!, paymentType!!, amount!!,customerName!!, phone!!)
    }
    fun observeAddOrderAction(): LiveData<Resource<OrderData>> {
        return orderActionObservable
    }
    fun observeCartAction(): LiveData<Resource<OrderData>> {
        return orderActionObservable
    }
    fun saveProducts(data: OrderData){
        orderActionRepository.saveProducts(data)

    }
    fun saveOrders(data: OrderData){
        orderActionRepository.saveOrders(data)

    }
    fun saveTotal(data: CartData){
        orderRepository.saveTotal(data)

    }
    fun getOfflineProducts() {
        productsRepository.getOfflineProducts()
    }
    fun getOfflineOrders() {
        productsRepository.getOfflineOrders()
    }
    fun observeOfflineProducts(): LiveData<Resource<List<Product>>> {
        return offlineProductsObservable
    }
    fun observeOfflineOrders(): LiveData<Resource<List<Order>>> {
        return offlineOrdersObservable
    }
    fun getOuthProfile(): LiveData<Profile> {
        return signInRepository.getProfile()
    }
}