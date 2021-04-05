package com.johan.reigntest.di.module

import com.johan.reigntest.ui.mvp.HomePresenter
import com.johan.reigntest.api.HomeApi
import com.johan.reigntest.di.scope.HomeScope
import com.johan.reigntest.ui.mvp.HomeContract
import com.johan.reigntest.ui.mvp.HomeModel
import dagger.Module
import dagger.Provides

@Module
class HomeModule(private val view: HomeContract.View) {
    @HomeScope
    @Provides
    fun provideView(): HomeContract.View = view

    @HomeScope
    @Provides
    fun provideModel(homeApi: HomeApi): HomeContract.Model = HomeModel(homeApi)

    @HomeScope
    @Provides
    fun providePresenter(view: HomeContract.View?, model: HomeContract.Model?): HomeContract.Presenter = HomePresenter(view, model)
}