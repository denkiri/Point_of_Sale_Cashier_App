package com.denkiri.poscashier.storage

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource

import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.expense.ExpenseData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExpenseRepository  (application: Application) {
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val expensesObservable = MutableLiveData<Resource<ExpenseData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }

    fun getActiveExpenses() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            active()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun active() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).activeExpenses()
            call.enqueue(object : Callback<ExpenseData> {
                override fun onFailure(call: Call<ExpenseData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<ExpenseData>?, response: Response<ExpenseData>?) {
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
        expensesObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: ExpenseData) {
        expensesObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        expensesObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}