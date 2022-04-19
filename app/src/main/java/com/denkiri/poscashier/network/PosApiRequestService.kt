package com.denkiri.poscashier.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object PosApiRequestService {
    private val BASE_URL = "https://denkiri.com/pos/api/v1/"
    var gson = GsonBuilder()
            .setLenient()
            .create()
    val retrofit: Retrofit
        get() = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    val service: EndPoints
        get() = retrofit.create(EndPoints::class.java)

    private fun getRetrofit(token: String): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(getClient(token))
                .build()
    }

    private fun getClient(token: String): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor { chain ->
            val newRequest = chain.request().newBuilder()
                    .addHeader("Authorization", token)
                    .build()
            chain.proceed(newRequest)
        }.build()
    }

    fun getService(token: String): EndPoints {
        return getRetrofit(token).create(EndPoints::class.java)
    }
}