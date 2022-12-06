package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Singleton
class ViewModelFactory @Inject constructor( private val map: Map<Class<*>,
        @JvmSuppressWildcards ViewModel>) :ViewModelProvider.Factory{

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return map[modelClass] as T
    }

}