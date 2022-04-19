package com.denkiri.poscashier.storage
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.R
import com.denkiri.poscashier.models.expense.IncomeData
import com.denkiri.poscashier.network.NetworkUtils
import com.denkiri.poscashier.network.RequestService
import com.denkiri.poscashier.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class IncomeActionRepository  (application: Application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val incomeActionObservable = MutableLiveData<Resource<IncomeData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addIncome(service: String,amount: String,month:String,date:String,year:String,cashier:String,items:String){
        setIsLoading(Observable.ADD_INCOME)

        if (NetworkUtils.isConnected(context)) {
            execute(service,amount, month, date, year, cashier, items)
        } else {
            setIsError(Observable.ADD_INCOME, context.getString(R.string.no_connection))
        }
    }
    fun editIncome(service: String,amount: String,month:String,date:String,year:String,cashier:String,items:String,cid:String) {
        setIsLoading(Observable.EDIT_INCOME)

        if (NetworkUtils.isConnected(context)) {
         //   updateIncome(service, amount, month, date, year, cashier, items, cid)
        } else {
            setIsError(Observable.EDIT_INCOME, context.getString(R.string.no_connection))
        }
    }
    fun deleteIncome(id:Int) {
        setIsLoading(Observable.DELETE_INCOME)

        if (NetworkUtils.isConnected(context)) {
         //   delete(id)
        } else {
            setIsError(Observable.DELETE_INCOME, context.getString(R.string.no_connection))
        }
    }
    fun execute(service: String,amount: String,month:String,date:String,year:String,cashier:String,items:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).recordIncome(service,amount, month, date, year, cashier, items)
            call.enqueue(object : Callback<IncomeData> {
                override fun onFailure(call: Call<IncomeData>?, t: Throwable?) {
                    setIsError(Observable.ADD_INCOME, t.toString())
                }
                override fun onResponse(call: Call<IncomeData>?, response: Response<IncomeData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_INCOME,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_INCOME,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_INCOME, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_INCOME, "Error Loading In")
                    }
                }
            })
        }

    }

  /*  fun delete( cid: Int){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteIncome( cid.toString()
            )
            call.enqueue(object : Callback<IncomeData> {
                override fun onFailure(call: Call<IncomeData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_INCOME, t.toString())
                }
                override fun onResponse(call: Call<IncomeData>?, response: Response<IncomeData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_INCOME,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_INCOME,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_INCOME, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_INCOME, "Error Loading In")
                    }
                }
            })
        }
    }
    fun updateIncome(service: String,amount: String,month:String,date:String,year:String,cashier:String,items:String,cid:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editIncome(service,amount, month, date, year, cashier, items, cid)
            call.enqueue(object : Callback<IncomeData> {
                override fun onFailure(call: Call<IncomeData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_INCOME, t.toString())
                }
                override fun onResponse(call: Call<IncomeData>?, response: Response<IncomeData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_INCOME,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_INCOME,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_INCOME, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_INCOME, "Error Loading In")
                    }
                }
            })
        }
    }

   */

    enum class Observable {
        DELETE_INCOME,
        EDIT_INCOME,
        ADD_INCOME
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_INCOME -> incomeActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_INCOME -> incomeActionObservable.postValue(Resource.loading(null))
            Observable.ADD_INCOME -> incomeActionObservable.postValue(Resource.loading(null))
                  }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_INCOME -> incomeActionObservable.postValue(Resource.success(data as IncomeData))
            Observable.DELETE_INCOME-> incomeActionObservable.postValue(Resource.success(data as IncomeData))
            Observable.ADD_INCOME->incomeActionObservable.postValue(Resource.success(data as IncomeData))
          }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_INCOME -> incomeActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_INCOME-> incomeActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_INCOME-> incomeActionObservable.postValue(Resource.error(message, null))
            }
    }
}