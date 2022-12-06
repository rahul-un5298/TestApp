package com.example.myapplication.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

class TestViewModel @Inject constructor (private val repository: DataRepository) : ViewModel() {

    private val _productList by lazy { MutableLiveData<List<String>>()}
    val productList : LiveData<List<String>>  by lazy { _productList }


    fun fetchListData() {
       viewModelScope.launch(Dispatchers.IO){
          repository.getListData()
       }
    }

    //var mutableLiveData = MutableLiveData()


}