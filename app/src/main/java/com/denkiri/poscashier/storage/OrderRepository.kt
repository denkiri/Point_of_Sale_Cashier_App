package com.denkiri.poscashier.storage
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.CartData
import com.denkiri.poscashier.models.order.Order
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.order.TotalAmount
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.OrderDao
import com.denkiri.poscashier.storage.daos.ProductDao
import com.denkiri.poscashier.storage.daos.ProfileDao
import com.denkiri.poscashier.storage.daos.TotalAmountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class OrderRepository (application: Application) {
    private val preferenceManager:PreferenceManager= PreferenceManager(application)
    private val context: Context
    private val profileDao: ProfileDao
    private val productDao: ProductDao
    private val orderDao: OrderDao
    private val totalAmountDao: TotalAmountDao
    private val db: PharmacyDatabase
    val orderObservable = MutableLiveData<Resource<CartData>>()
    val offlineProductsObservable = MutableLiveData<Resource<List<Product>>>()
    val offlineOrdersObservable = MutableLiveData<Resource<List<Order>>>()
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        productDao =db.productDao()
        profileDao = db.profileDao()
        orderDao=db.orderDao()
        totalAmountDao=db.totalAmountDao()
        context =application.applicationContext
    }
    fun saveOrder(data: CartData){
        GlobalScope.launch(context = Dispatchers.Main)
        {
        orderDao.delete()
        data.order?.let { orderDao.insertAll(it) }
    }}
    fun saveTotalAmount(totalAmount: TotalAmount) {
        GlobalScope.launch(context = Dispatchers.Main)
        {
        totalAmountDao.delete()
        totalAmount.totalAmount?.let { totalAmountDao.insertTotalAmount(totalAmount) }
    }}
    fun saveTotal(data: CartData) {
        GlobalScope.launch(context = Dispatchers.Main)
        {
            totalAmountDao.delete()
            data.totalAmount?.let { totalAmountDao.insertTotalAmount(data.totalAmount!!) }
        }
    }

    fun getOrder(invoice:String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute(invoice)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute(invoice:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).getOrder(invoice)
            call.enqueue(object : Callback<CartData> {
                override fun onFailure(call: Call<CartData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<CartData>?, response: Response<CartData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                saveOrder(response.body()!!)
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)
                                } else {
                                    saveTotalAmount(TotalAmount(0.0))
                                }
                                setIsSuccessful(response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(it) }
                            }
                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }
    fun getOfflineProducts() {
        offlineProductsObservable.postValue(Resource.success(productDao.getAll()))
    }
    fun getOfflineOrders() {
        offlineOrdersObservable.postValue(Resource.success(orderDao.getAll()))
    }
    private fun setIsLoading() {
        orderObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: CartData) {
        orderObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        orderObservable.postValue(Resource.error(message, null))
    }
    fun saveInvoiceNumber(invoice:String){
        preferenceManager.saveInvoiceNumber(invoice)
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}