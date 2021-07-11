package com.ab.migotest.model.api

import com.ab.migotest.model.vo.StatusItem
import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject

class ApiRepository @Inject constructor(private val okHttpClient: OkHttpClient) {

    private val wifiApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .baseUrl("http://192.168.2.2")
            .build()
            .create(ApiService::class.java)
    }
    private val cellNetworkApiService by lazy {
        Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .client(okHttpClient)
            .baseUrl("https://code-test.migoinc-dev.com")
            .build()
            .create(ApiService::class.java)
    }

    suspend fun fetchStatus(isWifi: Boolean): Response<StatusItem> {
        val apiService = if (isWifi) wifiApiService else cellNetworkApiService
        return apiService.fetchStatus()
    }
}