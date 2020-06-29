package com.test.app

import android.app.Application
import android.content.Context
import com.test.data.AppPreference
import com.test.data.IPreference
import com.test.di.RepositoryModule
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module(includes = [RepositoryModule::class])
class ApplicationModule(private val appContext: Context) {

    @Provides
    @Singleton
    fun provideApplication(): Application = appContext as Application

    @Provides
    @Singleton
    fun providerContext(): Context = appContext

    @Provides
    @Singleton
    fun providerPreference(): IPreference = AppPreference(appContext)
}
