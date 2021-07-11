package com.ab.migotest.model.api

import com.ab.migotest.model.vo.StatusItem
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("/status")
    suspend fun fetchStatus(): Response<StatusItem>

}