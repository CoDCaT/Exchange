package com.test.app

import com.test.MainScreenActivity
import com.test.MainScreenModule
import com.test.di.ActivityScope
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityBindingModule {

    @ActivityScope
    @ContributesAndroidInjector(modules = [MainScreenModule::class])
    fun bindLaunchScreenActivity(): MainScreenActivity
}