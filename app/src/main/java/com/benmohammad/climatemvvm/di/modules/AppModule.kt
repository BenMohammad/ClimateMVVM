package com.benmohammad.climatemvvm.di.modules

import android.content.Context
import dagger.Module
import dagger.Provides
import java.lang.reflect.Constructor
import javax.inject.Singleton

@Module
class AppModule(private val context: Context) {

    @Provides
    @Singleton
    fun provideAppContext() = context
}