package com.denkiri.poscashier.storage

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.report.SalesReportData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalesRepository (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val salesObservable = MutableLiveData<Resource<SalesReportData>>()

    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()

        context =application.applicationContext
    }

    fun getSales() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }


    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).salesReport()
            call.enqueue(object : Callback<SalesReportData> {
                override fun onFailure(call: Call<SalesReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<SalesReportData>?, response: Response<SalesReportData>?) {
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
        salesObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: SalesReportData) {
        salesObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        salesObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}