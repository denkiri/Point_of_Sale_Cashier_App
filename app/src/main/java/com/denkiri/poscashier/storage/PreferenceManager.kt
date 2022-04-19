package com.denkiri.poscashier.storage
import android.content.Context
import android.content.SharedPreferences
import com.denkiri.poscashier.models.oauth.Oauth
import com.google.gson.Gson
class PreferenceManager (internal var _context:Context){
    internal var pref: SharedPreferences
    internal var editor: SharedPreferences.Editor
    internal var PRIVATE_MODE = 0

    companion object {
        private val LOGIN_STATUS = "LOGIN_STATUS"
        private val INVOICE_NUMBER="INVOICE_NUMBER"
        private val CREDIT_DUE="CREDIT_DUE"
        private val EXPIRY="EXPIRY"
        private val PROFILE = "pharmacy_user_profile"
        private val FIREBASE_TOKEN = "firebasetoken"
        private val TOTAL_PRODUCT ="TOTAL_PRODUCT"
        private val PREF_NAME = "pharmacy_preferences"
    }

    init {
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE)
        editor = pref.edit()
    }

    fun clearUser() {
        editor.clear()
        editor.commit()
    }

    fun setFirebase(token: String) {
        editor.putString(FIREBASE_TOKEN, token)
        editor.commit()
    }
    fun getFirebase():String{
        return pref.getString(FIREBASE_TOKEN,"")!!
    }
    fun setLoginStatus(status:Int){
        editor.putInt(LOGIN_STATUS,status)
        editor.commit()
    }
    fun saveProfile(data: Oauth){
        editor.putString(Gson().toJson(data), PROFILE)
        editor.commit()
    }

    fun getLoginStatus():Int {
        return  pref.getInt(LOGIN_STATUS,0)

    }
    fun saveInvoiceNumber(invoice: String){
        editor.putString(INVOICE_NUMBER,invoice)
        editor.commit()
    }
    fun saveProductTotal(total: Int){
        editor.putInt(TOTAL_PRODUCT,total)
        editor.commit()
    }
    fun saveTotalAmount(total: String){
        editor.putString(CREDIT_DUE,total)
        editor.commit()
    }

    fun saveExpiry(total: Int){
        editor.putInt(EXPIRY,total)
        editor.commit()
    }
    fun getInvoiceNumber():String{
        return pref.getString(INVOICE_NUMBER,"")!!
    }
    fun getTotalAmount():String{
        return pref.getString(CREDIT_DUE,"")!!
    }
    fun getExpiry():Int{
        return pref.getInt(EXPIRY,0)
    }
    fun getProductTotal():Int{
        return pref.getInt(TOTAL_PRODUCT,0)
    }


}