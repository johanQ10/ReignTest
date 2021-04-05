package com.johan.reigntest.di.module

import android.app.Application
import android.content.Context
import com.johan.reigntest.di.App
import com.johan.reigntest.di.ApplicationContext
import com.johan.reigntest.di.scope.AppScope
import dagger.Module
import dagger.Provides

@Module
class AppModule(private val app: App) {
    @Provides
    @AppScope
    fun provideApplication(): Application = app

    @Provides
    @AppScope
    @ApplicationContext
    fun provideApplicationContext(): Context = app
}