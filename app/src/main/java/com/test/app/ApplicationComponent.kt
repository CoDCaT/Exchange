package com.test.app

import com.test.di.RoomModule
import com.test.di.RetrofitModule
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        ApplicationModule::class,
        AndroidSupportInjectionModule::class,
        ActivityBindingModule::class,
        RetrofitModule::class,
        RoomModule::class
    ]
)

interface ApplicationComponent : AndroidInjector<AppLoader>