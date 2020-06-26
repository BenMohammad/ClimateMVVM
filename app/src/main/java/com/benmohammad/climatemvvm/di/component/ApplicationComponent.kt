package com.benmohammad.climatemvvm.di.component

import com.benmohammad.climatemvvm.di.modules.AppModule
import com.benmohammad.climatemvvm.di.modules.DbModule
import com.benmohammad.climatemvvm.di.modules.OpenWeatherApiModule
import com.benmohammad.climatemvvm.di.modules.SubcomponentsModule
import com.benmohammad.climatemvvm.features.home.di.HomeComponent
import com.squareup.moshi.Moshi
import dagger.Component
import javax.inject.Singleton

@Component(modules = [AppModule::class, OpenWeatherApiModule::class, DbModule::class, SubcomponentsModule::class])
@Singleton
interface ApplicationComponent {
    fun getMoshi() : Moshi
    fun homeComponent(): HomeComponent.Factory
}