package com.johan.reigntest.di

import android.app.Application
import android.content.Context
import com.johan.reigntest.di.component.AppComponent
import com.johan.reigntest.di.component.DaggerAppComponent.builder
import com.johan.reigntest.di.module.AppModule

class App : Application() {
    private lateinit var appComponent: AppComponent

    companion object {
        private var INSTANCE: App? = null
        val appContext: Context
            get() = INSTANCE!!.applicationContext

        operator fun get(context: Context) = context.applicationContext as App

        fun get(): App? = INSTANCE
    }

    override fun onCreate() {
        super.onCreate()
        INSTANCE = this
        appComponent = builder().appModule(AppModule(this)).build()
    }

    fun component(): AppComponent = appComponent
}