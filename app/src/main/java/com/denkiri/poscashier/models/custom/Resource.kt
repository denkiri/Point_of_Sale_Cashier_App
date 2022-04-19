package com.denkiri.pharmacy.models.custom

import androidx.lifecycle.LiveData
import com.denkiri.poscashier.models.product.Product

class Resource<T> private constructor(val status: Status, val data: T?, val message: String?){
    companion object {


        fun <T> success(data: T): Resource<T> {
            return Resource(Status.SUCCESS, data, null)
        }

        fun <T> error(msg: String, data: T?): Resource<T> {
            return Resource(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(Status.LOADING, data, null)
        }
    }

}