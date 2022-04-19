package com.denkiri.poscashier.storage

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.oauth.Profile
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.order.TotalAmount
import com.denkiri.poscashier.models.product.Product
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
import retrofit2.http.Field

class OrderActionRepository (application: Application) {
    val orderActionObservable = MutableLiveData<Resource<OrderData>>()
    val productsObservable = MutableLiveData<Resource<List<Product>>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val productDao:ProductDao
    private val orderDao:OrderDao
    private val totalAmountDao:TotalAmountDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        context =application.applicationContext
        profileDao = db.profileDao()
        productDao =db.productDao()
        orderDao=db.orderDao()
        totalAmountDao=db.totalAmountDao()
    }

        fun saveTotalAmount(totalAmount: TotalAmount) {
            GlobalScope.launch(context = Dispatchers.Main)
            {
            totalAmountDao.delete()
            totalAmount.totalAmount?.let { totalAmountDao.insertTotalAmount(totalAmount) }
        }
    }
    fun getTotalAmount(): LiveData<TotalAmount> {

        return totalAmountDao.getTotalAmount()
    }
    fun addOrder(invoice:String,productCode:String,quantity:String,commision:String,vat:String,discount:String,price:String,cost:String,expirationDate:String,productName:String,descriptionName:String,category: String?,quantityLeft: String?, pid: String?,brand: String?) {
        setIsLoading(Observable.ADD_ORDER)

        if (NetworkUtils.isConnected(context)) {
            execute(invoice,productCode,quantity,commision,vat,discount,price,cost,expirationDate,productName,descriptionName, category, quantityLeft, pid,brand)
        } else {
            setIsError(Observable.ADD_ORDER, context.getString(R.string.no_connection))
        }
    }
    fun increaseCart(invoice:String,productCode:String,quantity:String,commision:String,vat:String,discount:String,price:String,cost:String,expirationDate:String,productName:String,descriptionName:String,category: String?,quantityLeft: String?, pid: String?) {
        setIsLoading(Observable.INCREASE_CART_ITEM)

        if (NetworkUtils.isConnected(context)) {
            increaseCartItem(invoice,productCode,quantity,commision,vat,discount,price,cost,expirationDate,productName,descriptionName, category, quantityLeft, pid)
        } else {
            setIsError(Observable.INCREASE_CART_ITEM, context.getString(R.string.no_connection))
        }
    }
    fun decreaseCart(invoice:String,productCode:String,quantity:String,commision:String,vat:String,discount:String,price:String,cost:String,expirationDate:String,productName:String,descriptionName:String,category: String?,quantityLeft: String?, pid: String?) {
        setIsLoading(Observable.DECREASE_CART_ITEM)

        if (NetworkUtils.isConnected(context)) {
            decreaseCartItem(invoice,productCode,quantity,commision,vat,discount,price,cost,expirationDate,productName,descriptionName, category, quantityLeft, pid)
        } else {
            setIsError(Observable.DECREASE_CART_ITEM, context.getString(R.string.no_connection))
        }
    }
    fun deleteCart(transactionId: String,quantity: String,productCode: String,invoice: String) {
        setIsLoading(Observable.DELETE_CART_ITEM)

        if (NetworkUtils.isConnected(context)) {
            deleteCartItem(transactionId,quantity,productCode,invoice)
        } else {
            setIsError(Observable.DELETE_CART_ITEM, context.getString(R.string.no_connection))
        }
    }
    fun execute(invoice:String,productCode:String,quantity:String,commision:String,vat:String,discount:String,price:String,cost:String,expirationDate:String,productName:String,descriptionName:String,category: String?,quantityLeft: String?, pid: String?,brand:String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addOrder(invoice,productCode,quantity,commision,vat,discount,price,cost,expirationDate,productName,descriptionName, category, quantityLeft, pid,brand)
            call.enqueue(object : Callback<OrderData> {
                override fun onFailure(call: Call<OrderData>?, t: Throwable?) {
                    setIsError(Observable.ADD_ORDER, t.toString())
                }
                override fun onResponse(call: Call<OrderData>?, response: Response<OrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalAmount != null) {
                                    preferenceManager.saveTotalAmount(response.body()!!.totalAmount.toString()!!)
                                } else {
                                    preferenceManager.saveTotalAmount("0")
                                }
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)
                                } else {
                                    saveTotalAmount(TotalAmount(0.0))
                                }
                                setIsSuccesful(Observable.ADD_ORDER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_ORDER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_ORDER, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_ORDER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun increaseCartItem(invoice:String,productCode:String,quantity:String,commision:String,vat:String,discount:String,price:String,cost:String,expirationDate:String,productName:String,descriptionName:String,category: String?,quantityLeft: String?, pid: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).increaseCartItem(invoice,productCode,quantity,commision,vat,discount,price,cost,expirationDate,productName,descriptionName, category, quantityLeft, pid)
            call.enqueue(object : Callback<OrderData> {
                override fun onFailure(call: Call<OrderData>?, t: Throwable?) {
                    setIsError(Observable.INCREASE_CART_ITEM, t.toString())
                }
                override fun onResponse(call: Call<OrderData>?, response: Response<OrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)
                                } else {
                                    saveTotalAmount(TotalAmount(0.0))
                                }
                                setIsSuccesful(Observable.INCREASE_CART_ITEM,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.INCREASE_CART_ITEM,it) }
                            }
                        }
                        else {
                            setIsError(Observable.INCREASE_CART_ITEM, response.toString())
                        }
                    } else {
                        setIsError(Observable.INCREASE_CART_ITEM, "Error Loading In")
                    }
                }
            })
        }

    }
    fun decreaseCartItem(invoice:String,productCode:String,quantity:String,commision:String,vat:String,discount:String,price:String,cost:String,expirationDate:String,productName:String,descriptionName:String,category: String?,quantityLeft: String?, pid: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).decreaseCartItem(invoice,productCode,quantity,commision,vat,discount,price,cost,expirationDate,productName,descriptionName, category, quantityLeft, pid)
            call.enqueue(object : Callback<OrderData> {
                override fun onFailure(call: Call<OrderData>?, t: Throwable?) {
                    setIsError(Observable.DECREASE_CART_ITEM, t.toString())
                }
                override fun onResponse(call: Call<OrderData>?, response: Response<OrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)
                                } else {
                                    saveTotalAmount(TotalAmount(0.0))
                                }
                                setIsSuccesful(Observable.DECREASE_CART_ITEM,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DECREASE_CART_ITEM,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DECREASE_CART_ITEM, response.toString())
                        }
                    } else {
                        setIsError(Observable.DECREASE_CART_ITEM, "Error Loading In")
                    }
                }
            })
        }

    }
    fun deleteCartItem(transactionId: String?,quantity: String?,productCode: String?,invoice: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteCartItem(transactionId,quantity,productCode,invoice)
            call.enqueue(object : Callback<OrderData> {
                override fun onFailure(call: Call<OrderData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_CART_ITEM, t.toString())
                }
                override fun onResponse(call: Call<OrderData>?, response: Response<OrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)
                                } else {
                                    saveTotalAmount(TotalAmount(0.0))
                                }
                                setIsSuccesful(Observable.DELETE_CART_ITEM,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_CART_ITEM,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_CART_ITEM, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_CART_ITEM, "Error Loading In")
                    }
                }
            })
        }

    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    fun saveProducts(data:OrderData ){
        GlobalScope.launch(context = Dispatchers.Main)
        {

        productDao.delete()
        data.products?.let { productDao.insertAll(it) }
    }}
    fun saveOrders(data:OrderData){
        GlobalScope.launch(context = Dispatchers.Main)
        {
        orderDao.delete()
        data.order?.let { orderDao.insertAll(it) }
    }}
    fun getProducts(){
     //   productsObservable.postValue(Resource.success(productDao.getAll()))

    }
    enum class Observable {

        ADD_ORDER,
        INCREASE_CART_ITEM,
        DECREASE_CART_ITEM,
        DELETE_CART_ITEM

    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.ADD_ORDER -> orderActionObservable.postValue(Resource.loading(null))
            Observable.INCREASE_CART_ITEM -> orderActionObservable.postValue(Resource.loading(null))
            Observable.DECREASE_CART_ITEM -> orderActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_CART_ITEM -> orderActionObservable.postValue(Resource.loading(null))
            }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.ADD_ORDER -> orderActionObservable.postValue(Resource.success(data as OrderData))
            Observable.INCREASE_CART_ITEM -> orderActionObservable.postValue(Resource.success(data as OrderData))
            Observable.DECREASE_CART_ITEM -> orderActionObservable.postValue(Resource.success(data as OrderData))
            Observable.DELETE_CART_ITEM -> orderActionObservable.postValue(Resource.success(data as OrderData))
          }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.ADD_ORDER -> orderActionObservable.postValue(Resource.error(message, null))
            Observable.INCREASE_CART_ITEM -> orderActionObservable.postValue(Resource.error(message, null))
            Observable.DECREASE_CART_ITEM -> orderActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_CART_ITEM -> orderActionObservable.postValue(Resource.error(message, null))
             }
    }
}