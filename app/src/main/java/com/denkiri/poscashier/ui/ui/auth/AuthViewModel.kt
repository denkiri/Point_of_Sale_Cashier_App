package com.denkiri.poscashier.ui.ui.auth
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.poscashier.models.oauth.Oauth
import com.denkiri.poscashier.models.oauth.Profile
import com.denkiri.poscashier.models.product.ProductData
import com.denkiri.poscashier.storage.ProductsRepository
import com.denkiri.poscashier.storage.SignInRepository
class AuthViewModel  (application: Application): AndroidViewModel(application){
    internal  var signInRepository: SignInRepository
    internal var productsRepository: ProductsRepository
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    private val changePasswordObservable = MediatorLiveData<Resource<Oauth>>()
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    init {
        productsRepository = ProductsRepository(application)
        signInRepository = SignInRepository(application)
        productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
        signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}
    }
    fun signIn(parameters: Oauth) {
        signInRepository.signIn(parameters)

    }
    fun observeSignIn(): LiveData<Resource<Oauth>> {
        return signInObservable
    }
    fun saveProfile(data:Oauth){
        signInRepository.saveProfile(data)

    }
    fun getOuthProfile(): LiveData<Profile> {
        return signInRepository.getProfile()
    }
    fun getProducts() {
        productsRepository.getProducts()
    }


}