package com.johan.reigntest.di.module

import com.johan.reigntest.api.HomeApi
import com.johan.reigntest.di.scope.AppScope
import dagger.Module
import dagger.Provides
import retrofit2.Retrofit

@Module(includes = [RetrofitModule::class])
object ApiModule {
    @JvmStatic
    @AppScope
    @Provides
    fun provideApi(retrofit: Retrofit): HomeApi {
        return retrofit.create(HomeApi::class.java)
    }
}