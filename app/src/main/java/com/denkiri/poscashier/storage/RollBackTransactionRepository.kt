package com.denkiri.poscashier.storage
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.RollBackItemsData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class RollBackTransactionRepository (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val rollBackTransactionObservable = MutableLiveData<Resource<RollBackItemsData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun getItems(invoice:String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute(invoice)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun deleteItems(transactionId: String,invoice: String,quantity: String, productCode: String,total: String, paymentType: String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            executeB(transactionId, invoice, quantity, productCode, total, paymentType)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun execute(invoice:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).rollBackItems(invoice)
            call.enqueue(object : Callback<RollBackItemsData> {
                override fun onFailure(call: Call<RollBackItemsData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<RollBackItemsData>?, response: Response<RollBackItemsData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
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
    private fun executeB(transactionId: String,invoice: String,quantity: String, productCode: String,total: String, paymentType: String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteInvoiceItems(transactionId, invoice, quantity, productCode, total, paymentType)
            call.enqueue(object : Callback<RollBackItemsData> {
                override fun onFailure(call: Call<RollBackItemsData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<RollBackItemsData>?, response: Response<RollBackItemsData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
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
    private fun setIsLoading() {
        rollBackTransactionObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: RollBackItemsData) {
        rollBackTransactionObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        rollBackTransactionObservable.postValue(Resource.error(message, null))
    }

    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}