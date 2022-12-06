package com.example.myapplication.dagger

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.example.myapplication.ui.MainActivity
import com.example.myapplication.viewmodel.TestViewModel
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.multibindings.ClassKey
import dagger.multibindings.IntoMap

@Module
abstract class ActivityModule {

//    @ActivityScope
//    @Provides
//    fun provideViewModel(activity: MainActivity): ViewModel{
//        return ViewModelProvider(activity)[TestViewModel::class.java]
//    }


    @ClassKey(TestViewModel::class)
    @Binds
    @IntoMap
    internal abstract fun getViewModel(viewModel: TestViewModel): ViewModel

}