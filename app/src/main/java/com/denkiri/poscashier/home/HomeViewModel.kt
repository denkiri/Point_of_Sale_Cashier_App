package com.denkiri.poscashier.home

import android.app.Application
import androidx.lifecycle.*
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.cashier.UsersData
import com.denkiri.poscashier.models.customer.CustomerData
import com.denkiri.poscashier.models.expense.ExpenseData
import com.denkiri.poscashier.models.expense.IncomeData
import com.denkiri.poscashier.models.oauth.Oauth
import com.denkiri.poscashier.models.order.*
import com.denkiri.poscashier.models.pending.PendingTransactionData
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.models.report.SalesReportData
import com.denkiri.poscashier.storage.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class HomeViewModel (application: Application) : AndroidViewModel(application){
    private val _time= MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("dd.MMM.YYYY")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _timeB= MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("MM/dd/YYYY")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _month= MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("MMMM")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _year= MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    val timeB:LiveData<String> =_timeB
    val time: LiveData<String> = _time
    val month: LiveData<String> = _month
    val year: LiveData<String> = _year
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    internal var customerRepository: CustomerRepository
    internal var pendingTransactionRepository:PendingTransactionRepository
    internal var pendingOrdersRepository:PendingOrdersRepository
    internal var productsRepository: ProductsRepository
    internal var orderRepository :OrderRepository
    internal var orderActionRepository: OrderActionRepository
    internal var productsActionRepository: ProductActionRepository
    internal var transactionRepository:TransactionRepository
    internal var salesRepository: SalesRepository
    internal var rollBackTransactionRepository:RollBackTransactionRepository
    internal var incomeActionRepository:IncomeActionRepository
    internal var expenseRepository: ExpenseRepository
    internal var usersRepository: UsersRepository
    internal var expenseActionRepository: ExpenseActionRepository
    private val usersObservable = MediatorLiveData<Resource<UsersData>>()
    private val customerObservable = MediatorLiveData<Resource<CustomerData>>()
    private val pendingTransactionObservable=MediatorLiveData<Resource<PendingTransactionData>>()
    private val productActionObservable= MediatorLiveData<Resource<ProductData>>()
    private val orderActionObservable= MediatorLiveData<Resource<OrderData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    private val orderObservable = MediatorLiveData<Resource<CartData>>()
    private val offlineProductsObservable = MediatorLiveData<Resource<List<Product>>>()
    private val offlineOrdersObservable = MediatorLiveData<Resource<List<Order>>>()
    private val pendingOrdersObservable = MediatorLiveData<Resource<PendingOrdersData>>()
    private val transactionObservable=MediatorLiveData<Resource<TransactionData>>()
    private val salesObservable = MediatorLiveData<Resource<SalesReportData>>()
    private val rollBackTransactionObservable=MediatorLiveData<Resource<RollBackItemsData>>()
    private val incomeActionObservable = MediatorLiveData<Resource<IncomeData>>()
    private val expenseObservable = MediatorLiveData<Resource<ExpenseData>>()
    private val expenseActionObservable= MediatorLiveData<Resource<ExpenseData>>()

    init {
        expenseActionRepository= ExpenseActionRepository(application)
        productsActionRepository = ProductActionRepository(application)
        orderActionRepository= OrderActionRepository(application)
        productsRepository = ProductsRepository(application)
        orderRepository = OrderRepository(application)
        customerRepository = CustomerRepository(application)
        pendingOrdersRepository =PendingOrdersRepository(application)
        pendingTransactionRepository= PendingTransactionRepository(application)
        transactionRepository= TransactionRepository(application)
        salesRepository = SalesRepository(application)
        rollBackTransactionRepository= RollBackTransactionRepository(application)
        incomeActionRepository= IncomeActionRepository(application)
        expenseRepository = ExpenseRepository(application)
        usersRepository= UsersRepository(application)
        usersObservable.addSource(usersRepository.usersObservable) { data -> usersObservable.setValue(data) }
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
        orderObservable.addSource(orderRepository.orderObservable) { data -> orderObservable.setValue(data) }
        offlineProductsObservable.addSource(productsRepository.offlineProductsObservable) { data -> offlineProductsObservable.setValue(data) }
        offlineOrdersObservable.addSource(productsRepository.offlineOrdersObservable) { data -> offlineOrdersObservable.setValue(data) }
        productActionObservable.addSource(productsActionRepository.productActionObservable) { data -> productActionObservable.setValue(data) }
        orderActionObservable.addSource(orderActionRepository.orderActionObservable) { data -> orderActionObservable.setValue(data) }
        customerObservable.addSource(customerRepository.customerObservable) { data -> customerObservable.setValue(data) }
        pendingTransactionObservable.addSource(pendingTransactionRepository.pendingTransactionsObservable){data ->pendingTransactionObservable.setValue(data)}
        pendingOrdersObservable.addSource(pendingOrdersRepository.pendingOrdersObservable){data ->pendingOrdersObservable.setValue(data)}
        transactionObservable.addSource(transactionRepository.transactionActionObservable){data ->transactionObservable.setValue(data)}
        salesObservable.addSource(salesRepository.salesObservable) { data -> salesObservable.setValue(data) }
        rollBackTransactionObservable.addSource(rollBackTransactionRepository.rollBackTransactionObservable){ data -> rollBackTransactionObservable.setValue(data) }
        incomeActionObservable.addSource(incomeActionRepository.incomeActionObservable) { data -> incomeActionObservable.setValue(data) }
        expenseObservable.addSource(expenseRepository.expensesObservable) { data -> expenseObservable.setValue(data) }
        expenseActionObservable.addSource(expenseActionRepository.expenseActionObservable){ data ->expenseActionObservable.setValue(data)}
    }
    fun observeCustomer(): LiveData<Resource<CustomerData>> {
        return customerObservable
    }
    fun getCustomer() {
        customerRepository.getCustomers()
    }
    fun observeUsers(): LiveData<Resource<UsersData>> {
        return usersObservable
    }
    fun getUsers() {
        usersRepository.getUsers()
    }
    fun getSalesReport() {
        salesRepository.getSales()
    }
    fun observeSalesReport(): LiveData<Resource<SalesReportData>> {
        return salesObservable
    }
    fun observeTransactionData(): LiveData<Resource<TransactionData>> {
        return transactionObservable
    }
    fun pendingMpesaSale(amount: String?,phone: String?,invoice: String?){
        transactionRepository.pendingMpesaSale(amount!!,phone!!,invoice!!)
    }
    fun observePendingOrders(): LiveData<Resource<PendingOrdersData>> {
        return pendingOrdersObservable
    }
    fun getPendingOrders() {
        pendingOrdersRepository.getPendingOrders()
    }
    fun observePendingTransaction(): LiveData<Resource<PendingTransactionData>> {
        return pendingTransactionObservable
    }
    fun getPendingTransactions() {
        pendingTransactionRepository.getPendingTransactions()
    }
    fun addIncome(service: String?,amount: String?,month:String?,date:String?,year:String?,cashier:String?,items:String?){
        incomeActionRepository.addIncome(service!!,amount!!,month!!,date!!,year!!,cashier!!,items!!)
    }
    fun observeIncomeActionData(): LiveData<Resource<IncomeData>> {
        return incomeActionObservable
    }
    fun getProducts() {
        productsRepository.getProducts()
    }
    fun getTotalAmount(): LiveData<TotalAmount> {
        return orderActionRepository.getTotalAmount()
    }

    fun getTotal(): String {
        return preferenceManager.getTotalAmount()
    }
    fun getOrder(invoice:String?) {
        orderRepository.getOrder(invoice!!)
    }
    fun getItems(invoice:String?) {
        rollBackTransactionRepository.getItems(invoice!!)
    }
    fun observeItemsData(): LiveData<Resource<RollBackItemsData>> {
        return rollBackTransactionObservable
    }
    fun observeCartData(): LiveData<Resource<CartData>> {
        return orderObservable
    }
    fun saveInvoiceNumber(invoice:String){
        productsRepository.saveInvoiceNumber(invoice)
    }
    fun addExpense(name:String?,amount:String?){
        expenseActionRepository.addExpense(name!!,amount!!)
    }
    fun observeExpenseAction(): LiveData<Resource<ExpenseData>> {
        return expenseActionObservable
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
    fun getActiveExpenses(){
        expenseRepository.getActiveExpenses()
    }
    fun observeExpenses(): LiveData<Resource<ExpenseData>> {
        return expenseObservable
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
    fun deleteInvoiceItems(transactionId: String?,invoice: String?,quantity: String?, productCode: String?,total: String?, paymentType: String?) {
        rollBackTransactionRepository.deleteItems(transactionId!!,invoice!!,quantity!!,productCode!!,total!!,paymentType!!)
    }
}