package com.denkiri.poscashier.storage
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.poscashier.network.PosApiRequestService
import com.denkiri.poscashier.R
import com.denkiri.poscashier.network.NetworkUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class SubscriptionActionRepository (application: Application){
    val subscriptionActionObservable = MutableLiveData<Resource<PaymentResponseData>>()
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        context =application.applicationContext
    }
    fun subscription(code:String) {
        setIsLoading(Observable.CHECK_SUBSCRIPTION)
        if (NetworkUtils.isConnected(context)) {
            checkSubscription(code)
        } else {
            setIsError(Observable.CHECK_SUBSCRIPTION, context.getString(R.string.no_connection))
        }
    }

    fun checkSubscription(code:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = PosApiRequestService.getService("").checkSubscription(code)
            call.enqueue(object : Callback<PaymentResponseData> {
                override fun onFailure(call: Call<PaymentResponseData>?, t: Throwable?) {
                    setIsError(Observable.CHECK_SUBSCRIPTION, t.toString())
                }
                override fun onResponse(call: Call<PaymentResponseData>?, response: Response<PaymentResponseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccessful(Observable.CHECK_SUBSCRIPTION,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.CHECK_SUBSCRIPTION,it) }
                            }
                        }
                        else {
                            setIsError(Observable.CHECK_SUBSCRIPTION, response.toString())
                        }
                    } else {
                        setIsError(Observable.CHECK_SUBSCRIPTION, "Error Loading In")
                    }
                }
            })
        }

    }


    enum class Observable {
        CHECK_SUBSCRIPTION


    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.CHECK_SUBSCRIPTION-> subscriptionActionObservable.postValue(Resource.loading(null))

        }
    }
    private fun <T> setIsSuccessful(observable: Observable, data: T?) {
        when (observable) {
           Observable. CHECK_SUBSCRIPTION -> subscriptionActionObservable.postValue(Resource.success(data as PaymentResponseData))

        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
           Observable.CHECK_SUBSCRIPTION  -> subscriptionActionObservable.postValue(Resource.error(message, null))

        }
    }
}