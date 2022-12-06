package com.example.myapplication.data.repository

import com.example.myapplication.data.api.ApiService
import javax.inject.Inject

class DataRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getListData() = apiService.getListData()

}