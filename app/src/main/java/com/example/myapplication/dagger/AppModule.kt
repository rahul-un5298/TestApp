package com.example.myapplication.dagger

import com.example.myapplication.ui.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
abstract class AppModule {

    @ActivityScope
    @ContributesAndroidInjector()
    abstract fun contributeMainActivity(): MainActivity

}