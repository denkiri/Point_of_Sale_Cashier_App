package com.denkiri.poscashier.storage
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.Order
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.product.Product
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.OrderDao
import com.denkiri.poscashier.storage.daos.ProductDao
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class ProductsRepository (application: Application)  {
    private val preferenceManager:PreferenceManager= PreferenceManager(application)
    private val context: Context
    private val profileDao: ProfileDao
    private val productDao: ProductDao
    private val orderDao: OrderDao
    private val db: PharmacyDatabase
    val productObservable = MutableLiveData<Resource<ProductData>>()
    val offlineProductsObservable = MutableLiveData<Resource<List<Product>>>()
    val offlineOrdersObservable = MutableLiveData<Resource<List<Order>>>()
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        productDao =db.productDao()
        profileDao = db.profileDao()
        orderDao=db.orderDao()
        context =application.applicationContext
    }
    fun saveProducts(data: ProductData){
        GlobalScope.launch(context = Dispatchers.Main)
        {
        productDao.delete()
        data.products?.let { productDao.insertAll(it) }
    }}
    fun getProducts() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).products()
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                saveProducts(response.body()!!)

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
        GlobalScope.launch(context = Dispatchers.Main)
        {
        offlineProductsObservable.postValue(Resource.success(productDao.getAll()))
    }}
    fun getOfflineOrders() {
        GlobalScope.launch(context = Dispatchers.Main)
        {
        offlineOrdersObservable.postValue(Resource.success(orderDao.getAll()))
    }}
    private fun setIsLoading() {
        productObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: ProductData) {
        productObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        productObservable.postValue(Resource.error(message, null))
    }
    fun saveInvoiceNumber(invoice:String){
        GlobalScope.launch(context = Dispatchers.Main)
        {
        preferenceManager.saveInvoiceNumber(invoice)
    }}
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}