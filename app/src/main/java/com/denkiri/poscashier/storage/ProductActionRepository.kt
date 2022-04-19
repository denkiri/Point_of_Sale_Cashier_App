package com.denkiri.poscashier.storage

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProductActionRepository(application: Application) {
    val productActionObservable = MutableLiveData<Resource<ProductData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        context =application.applicationContext
        profileDao = db.profileDao()
    }
    /*
    fun addProduct(price:String,cost:String,productName:String,productDescription:String,supplier:String,quantity:String,category: String,deliveryDate: String,expiryDate:String,unit: String,reorder: String) {
        setIsLoading(Observable.ADD_PRODUCT)

        if (NetworkUtils.isConnected(context)) {
            execute(price,cost,productName,productDescription,supplier,quantity,category,deliveryDate,expiryDate,unit,reorder)
        } else {
            setIsError(Observable.ADD_PRODUCT, context.getString(R.string.no_connection))
        }
    }
    fun editProduct(price:String,cost:String,productName:String,productDescription:String,supplier:String,quantity:String,category: String,unit: String,reorder: String,expiryDate:String,pid:String) {
        setIsLoading(Observable.EDIT_PRODUCT)

        if (NetworkUtils.isConnected(context)) {
            edit(price,cost,productName,productDescription,supplier,quantity,category,unit,reorder,expiryDate,pid)
        } else {
            setIsError(Observable.EDIT_PRODUCT, context.getString(R.string.no_connection))
        }
    }
    fun pullOutProduct(productCode:String,productName:String,productDescription:String,cost:String,quantity:String,category: String,expiryDate:String) {
        setIsLoading(Observable.PULL_OUT_STOCK)

        if (NetworkUtils.isConnected(context)) {
            pullOutStock(productCode,productName,productDescription,cost,quantity,category,expiryDate)
        } else {
            setIsError(Observable.PULL_OUT_STOCK, context.getString(R.string.no_connection))
        }
    }
    fun increaseStock(price:String,cost:String,quantity:String,expiryDate:String,pid:String) {
        setIsLoading(Observable.INCREASE_PRODUCT)

        if (NetworkUtils.isConnected(context)) {
            increaseProduct(price,cost,quantity,expiryDate,pid)
        } else {
            setIsError(Observable.INCREASE_PRODUCT, context.getString(R.string.no_connection))
        }
    }
    fun deleteProduct(pid:String) {
        setIsLoading(Observable.DELETE_PRODUCT)

        if (NetworkUtils.isConnected(context)) {
            delete(pid)
        } else {
            setIsError(Observable.DELETE_PRODUCT, context.getString(R.string.no_connection))
        }
    }
    fun execute(price:String,cost:String,bname:String,dname:String,supplier:String,qty:String,categ:String,date_del:String,ex_date:String,unit:String,reorder:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addProduct(price,cost,bname,dname,supplier,qty,categ,date_del,ex_date,unit,reorder)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(Observable.ADD_PRODUCT, t.toString())
                }
                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_PRODUCT,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_PRODUCT,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_PRODUCT, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_PRODUCT, "Error Loading In")
                    }
                }
            })
        }

    }
    fun edit(price:String,cost:String,bname:String,dname:String,supplier:String,qty:String,categ:String,unit:String,reorder:String,ex_date:String,pid:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editProduct(price,cost,bname,dname,supplier,qty,categ,unit,reorder,ex_date,pid)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_PRODUCT, t.toString())
                }
                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_PRODUCT,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_PRODUCT,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_PRODUCT, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_PRODUCT, "Error Loading In")
                    }
                }
            })
        }

    }
    fun increaseProduct(price:String,cost:String,qty:String,ex_date:String,pid:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).increaseProduct(price,cost,qty,ex_date,pid)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(Observable.INCREASE_PRODUCT, t.toString())
                }
                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.INCREASE_PRODUCT,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.INCREASE_PRODUCT,it) }
                            }
                        }
                        else {
                            setIsError(Observable.INCREASE_PRODUCT, response.toString())
                        }
                    } else {
                        setIsError(Observable.INCREASE_PRODUCT, "Error Loading In")
                    }
                }
            })
        }

    }
    fun pullOutStock(code:String,bname:String,dname:String,cost:String,qty:String,categ:String,ex_date:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).pullOutProduct(code,bname,dname,cost,qty,categ,ex_date)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(Observable.PULL_OUT_STOCK, t.toString())
                }
                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.PULL_OUT_STOCK,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.PULL_OUT_STOCK,it) }
                            }
                        }
                        else {
                            setIsError(Observable.PULL_OUT_STOCK, response.toString())
                        }
                    } else {
                        setIsError(Observable.PULL_OUT_STOCK, "Error Loading In")
                    }
                }
            })
        }

    }
    fun delete(pid:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteProduct(pid)
            call.enqueue(object : Callback<ProductData> {
                override fun onFailure(call: Call<ProductData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_PRODUCT, t.toString())
                }
                override fun onResponse(call: Call<ProductData>?, response: Response<ProductData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_PRODUCT,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_PRODUCT,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_PRODUCT, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_PRODUCT, "Error Loading In")
                    }
                }
            })
        }

    }

     */
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    enum class Observable {
        EDIT_PRODUCT,
        ADD_PRODUCT,
        INCREASE_PRODUCT,
        DELETE_PRODUCT,
        PULL_OUT_STOCK
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.ADD_PRODUCT-> productActionObservable.postValue(Resource.loading(null))
           Observable.EDIT_PRODUCT -> productActionObservable.postValue(Resource.loading(null))
            Observable.INCREASE_PRODUCT -> productActionObservable.postValue(Resource.loading(null))
            Observable.PULL_OUT_STOCK -> productActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_PRODUCT -> productActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.ADD_PRODUCT -> productActionObservable.postValue(Resource.success(data as ProductData))
           Observable.EDIT_PRODUCT -> productActionObservable.postValue(Resource.success(data as ProductData))
            Observable.INCREASE_PRODUCT -> productActionObservable.postValue(Resource.success(data as ProductData))
            Observable.PULL_OUT_STOCK -> productActionObservable.postValue(Resource.success(data as ProductData))
            Observable.DELETE_PRODUCT -> productActionObservable.postValue(Resource.success(data as ProductData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.ADD_PRODUCT -> productActionObservable.postValue(Resource.error(message, null))
           Observable.EDIT_PRODUCT-> productActionObservable.postValue(Resource.error(message, null))
            Observable.INCREASE_PRODUCT-> productActionObservable.postValue(Resource.error(message, null))
            Observable.PULL_OUT_STOCK-> productActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_PRODUCT-> productActionObservable.postValue(Resource.error(message, null))
        }
    }
}