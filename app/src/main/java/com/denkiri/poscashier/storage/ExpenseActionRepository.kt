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
class ExpenseActionRepository  (application: Application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val expenseActionObservable = MutableLiveData<Resource<ExpenseData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addExpense(name: String,amount: String){
        setIsLoading(Observable.ADD_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            execute(name,amount)
        } else {
            setIsError(Observable.ADD_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun execute(name:String,amount: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addExpense(name,amount)
            call.enqueue(object : Callback< ExpenseData> {
                override fun onFailure(call: Call< ExpenseData>?, t: Throwable?) {
                    setIsError(Observable.ADD_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call< ExpenseData>?, response: Response< ExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_EXPENSE, "Error Loading In")
                    }
                }
            })
        }

    }






    enum class Observable {
        DELETE_EXPENSE,
        EDIT_EXPENSE,
        ACTIVATE_EXPENSE,
        DEACTIVATE_EXPENSE,
        ADD_EXPENSE
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.ADD_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.ACTIVATE_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.DEACTIVATE_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_EXPENSE -> expenseActionObservable.postValue(Resource.success(data as ExpenseData))
            Observable.DELETE_EXPENSE-> expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
            Observable.ADD_EXPENSE->expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
            Observable.ACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
            Observable.DEACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_EXPENSE -> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.ACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.DEACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
        }
    }
}