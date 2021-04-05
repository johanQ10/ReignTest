package com.johan.reigntest.di.component

import android.app.Application
import com.johan.reigntest.di.App
import com.johan.reigntest.di.module.ApiModule
import com.johan.reigntest.di.module.AppModule
import com.johan.reigntest.api.HomeApi
import com.johan.reigntest.di.scope.AppScope
import dagger.Component

@AppScope
@Component(modules = [AppModule::class, ApiModule::class])
interface AppComponent {
    fun inject(app: App?)
    val application: Application?

    fun homeApi(): HomeApi
}