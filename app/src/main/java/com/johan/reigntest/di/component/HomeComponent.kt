package com.johan.reigntest.di.component

import com.johan.reigntest.di.module.HomeModule
import com.johan.reigntest.di.scope.HomeScope
import com.johan.reigntest.ui.HomeActivity
import dagger.Component

@HomeScope
@Component(dependencies = [AppComponent::class], modules = [HomeModule::class])
interface HomeComponent {
    fun inject(homeActivity: HomeActivity)
}