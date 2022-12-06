package com.example.myapplication.base

import android.app.Application
import com.example.myapplication.dagger.AppComponent
import com.example.myapplication.dagger.DaggerAppComponent

class TestApplication : Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        app = this
        appComponent = DaggerAppComponent.builder().applicationBind(this).build()
        appComponent.inject(this)
        super.onCreate()
    }

    companion object {
        private var app : TestApplication? = null
        fun getApplication(): TestApplication? {
            return app
        }
    }
}



