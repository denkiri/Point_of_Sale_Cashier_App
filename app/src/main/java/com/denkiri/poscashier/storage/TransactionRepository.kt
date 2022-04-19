package com.denkiri.poscashier.storage
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.TransactionData
import com.denkiri.poscashier.models.order.OrderData
import com.denkiri.poscashier.models.order.TotalAmount
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

class TransactionRepository (application: Application) {
    val transactionActionObservable = MutableLiveData<Resource<TransactionData>>()
    private val context: Context
    private val db: PharmacyDatabase
    private val profileDao: ProfileDao
    private val orderDao: OrderDao
    private val totalAmountDao: TotalAmountDao
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        context =application.applicationContext
        profileDao = db.profileDao()
        orderDao=db.orderDao()
        totalAmountDao=db.totalAmountDao()
    }
    fun saveCreditSale(invoice: String,cashier: String,paymentType: String,amount: String,dueDate: String,pAmount: String,customerName: String) {
        setIsLoading(Observable.CREDIT)

        if (NetworkUtils.isConnected(context)) {
            creditSale(invoice, cashier, paymentType, amount, dueDate, pAmount, customerName)
        } else {
            setIsError(Observable.CREDIT, context.getString(R.string.no_connection))
        }
    }
    fun saveCashSale(invoice: String,cashier: String,paymentType: String,amount: String,dueDate: String,pAmount: String,customerName: String) {
        setIsLoading(Observable.CASH)

        if (NetworkUtils.isConnected(context)) {
            cashSale(invoice, cashier, paymentType, amount, dueDate, pAmount, customerName)
        } else {
            setIsError(Observable.CASH, context.getString(R.string.no_connection))
        }
    }
    fun saveMpesaSale(invoice: String,cashier: String,paymentType: String,amount: String,customerName: String,phone: String) {
        setIsLoading(Observable.MPESA)

        if (NetworkUtils.isConnected(context)) {
            mpesaSale(invoice, cashier, paymentType, amount,customerName,phone)
        } else {
            setIsError(Observable.MPESA, context.getString(R.string.no_connection))
        }
    }
    fun pendingMpesaSale(amount: String,phone: String,invoice: String) {
        setIsLoading(Observable.COMPLETE_MPESA_TRANSACTION)

        if (NetworkUtils.isConnected(context)) {
            completeMpesaSale(amount,phone,invoice)
        } else {
            setIsError(Observable.COMPLETE_MPESA_TRANSACTION, context.getString(R.string.no_connection))
        }
    }
    fun creditSale(invoice: String?,cashier: String?,paymentType: String?,amount: String?,dueDate: String?,pAmount: String?,customerName: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).saveCreditSale(invoice, cashier, paymentType, amount, dueDate, pAmount, customerName)
            call.enqueue(object : Callback<TransactionData> {
                override fun onFailure(call: Call<TransactionData>?, t: Throwable?) {
                    setIsError(Observable.CREDIT, t.toString())
                }
                override fun onResponse(call: Call<TransactionData>?, response: Response<TransactionData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                orderDao.delete()
                                totalAmountDao.delete()
                             //   if (response.body()!!.totalAmount != null) {
                                //    saveTotalAmount(response.body()!!.totalAmount!!)
                              //  } else {
                               //     saveTotalAmount(TotalAmount(0.0))
                             //   }
                                setIsSuccessful(Observable.CREDIT,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.CREDIT,it) }
                            }
                        }
                        else {
                            setIsError(Observable.CREDIT, response.toString())
                        }
                    } else {
                        setIsError(Observable.CREDIT, "Error Loading In")
                    }
                }
            })
        }

    }
    fun cashSale(invoice: String?,cashier: String?,paymentType: String?,amount: String?,dueDate: String?,pAmount: String?,customerName: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).saveCashSale(invoice, cashier, paymentType, amount, dueDate, pAmount, customerName)
            call.enqueue(object : Callback<TransactionData> {
                override fun onFailure(call: Call<TransactionData>?, t: Throwable?) {
                    setIsError(Observable.CASH, t.toString())
                }
                override fun onResponse(call: Call<TransactionData>?, response: Response<TransactionData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                orderDao.delete()
                                totalAmountDao.delete()
                                //   if (response.body()!!.totalAmount != null) {
                                //    saveTotalAmount(response.body()!!.totalAmount!!)
                                //  } else {
                                //     saveTotalAmount(TotalAmount(0.0))
                                //   }
                                setIsSuccessful(Observable.CASH,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.CASH,it) }
                            }
                        }
                        else {
                            setIsError(Observable.CASH, response.toString())
                        }
                    } else {
                        setIsError(Observable.CASH, "Error Loading In")
                    }
                }
            })
        }

    }
    fun mpesaSale(invoice: String?,cashier: String?,paymentType: String?,amount: String?,customerName: String?,phone: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).saveMpesaSale(invoice, cashier, paymentType, amount,customerName,phone)
            call.enqueue(object : Callback<TransactionData> {
                override fun onFailure(call: Call<TransactionData>?, t: Throwable?) {
                    setIsError(Observable.MPESA, t.toString())
                }
                override fun onResponse(call: Call<TransactionData>?, response: Response<TransactionData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                orderDao.delete()
                                totalAmountDao.delete()
                                //   if (response.body()!!.totalAmount != null) {
                                //    saveTotalAmount(response.body()!!.totalAmount!!)
                                //  } else {
                                //     saveTotalAmount(TotalAmount(0.0))
                                //   }
                                setIsSuccessful(Observable.MPESA,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.MPESA,it) }
                            }
                        }
                        else {
                            setIsError(Observable.MPESA, response.toString())
                        }
                    } else {
                        setIsError(Observable.MPESA, "Error Loading In")
                    }
                }
            })
        }

    }
    fun completeMpesaSale(amount: String?,phone: String?,invoice: String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).savePendingMpesaSale(amount,phone,invoice)
            call.enqueue(object : Callback<TransactionData> {
                override fun onFailure(call: Call<TransactionData>?, t: Throwable?) {
                    setIsError(Observable.COMPLETE_MPESA_TRANSACTION, t.toString())
                }
                override fun onResponse(call: Call<TransactionData>?, response: Response<TransactionData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                //   if (response.body()!!.totalAmount != null) {
                                //    saveTotalAmount(response.body()!!.totalAmount!!)
                                //  } else {
                                //     saveTotalAmount(TotalAmount(0.0))
                                //   }
                                setIsSuccessful(Observable.COMPLETE_MPESA_TRANSACTION,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.COMPLETE_MPESA_TRANSACTION,it) }
                            }
                        }
                        else {
                            setIsError(Observable.COMPLETE_MPESA_TRANSACTION, response.toString())
                        }
                    } else {
                        setIsError(Observable.COMPLETE_MPESA_TRANSACTION, "Error Loading In")
                    }
                }
            })
        }

    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    enum class Observable {
        CASH,
        CREDIT,
        MPESA,
        COMPLETE_MPESA_TRANSACTION

    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.CASH -> transactionActionObservable.postValue(Resource.loading(null))
            Observable.CREDIT -> transactionActionObservable.postValue(Resource.loading(null))
            Observable.MPESA -> transactionActionObservable.postValue(Resource.loading(null))
            Observable.COMPLETE_MPESA_TRANSACTION -> transactionActionObservable.postValue(Resource.loading(null))
             }
    }
    private fun <T> setIsSuccessful(observable: Observable, data: T?) {
        when (observable) {
            Observable.CASH -> transactionActionObservable.postValue(Resource.success(data as TransactionData))
            Observable.CREDIT -> transactionActionObservable.postValue(Resource.success(data as TransactionData))
            Observable.MPESA -> transactionActionObservable.postValue(Resource.success(data as TransactionData))
            Observable.COMPLETE_MPESA_TRANSACTION -> transactionActionObservable.postValue(Resource.success(data as TransactionData))
           }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.CASH -> transactionActionObservable.postValue(Resource.error(message, null))
            Observable.CREDIT -> transactionActionObservable.postValue(Resource.error(message, null))
            Observable.MPESA ->transactionActionObservable.postValue(Resource.error(message, null))
            Observable.COMPLETE_MPESA_TRANSACTION ->transactionActionObservable.postValue(Resource.error(message, null))
        }
    }
}