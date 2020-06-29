package com.test.app

import com.test.BuildConfig
import com.test.logger.DebugTree
import com.test.logger.ReleaseTree
import dagger.android.AndroidInjector
import dagger.android.support.DaggerApplication
import timber.log.Timber

class AppLoader : DaggerApplication() {

    override fun onCreate() {
        super.onCreate()
        initTimber()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent
            .builder()
            .applicationModule(ApplicationModule(this))
            .build()
    }

    private fun initTimber() {
        if (BuildConfig.DEBUG) Timber.plant(DebugTree())
        else Timber.plant(ReleaseTree())
    }
}