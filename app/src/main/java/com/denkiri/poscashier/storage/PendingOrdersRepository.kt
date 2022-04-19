package com.denkiri.poscashier.storage

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.order.PendingOrdersData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PendingOrdersRepository (application: Application) {
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val pendingOrdersObservable = MutableLiveData<Resource<PendingOrdersData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun getPendingOrders() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).pendingOrders()
            call.enqueue(object : Callback<PendingOrdersData> {
                override fun onFailure(call: Call<PendingOrdersData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<PendingOrdersData>?, response: Response<PendingOrdersData>?) {
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
        pendingOrdersObservable .postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters:PendingOrdersData) {
        pendingOrdersObservable .postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        pendingOrdersObservable .postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}