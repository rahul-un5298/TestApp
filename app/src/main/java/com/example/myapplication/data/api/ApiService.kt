package com.example.myapplication.data.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET("data")
    suspend fun getListData(): Response<List<String>>
}