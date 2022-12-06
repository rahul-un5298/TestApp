package com.example.myapplication.dagger

import android.app.Application
import android.view.View
import androidx.lifecycle.ViewModel
import com.example.myapplication.ui.MainActivity
import dagger.BindsInstance
import dagger.Component
import dagger.Component.Builder
import dagger.android.AndroidInjectionModule
import javax.inject.Scope
import javax.inject.Singleton

@Scope
@MustBeDocumented
@Retention(value = AnnotationRetention.RUNTIME)
annotation class ActivityScope


@Singleton
@Component( modules = [AndroidInjectionModule::class, AppModule::class, ActivityModule::class, NetModule::class])
interface AppComponent {

  fun inject(application: Application)
  fun inject(activity: MainActivity)
  fun getMap(): Map<Class<*>, ViewModel>

  @Component.Builder
  interface Builder{
    fun build() :AppComponent

    @BindsInstance
    fun applicationBind(application: Application):Builder

  }



}