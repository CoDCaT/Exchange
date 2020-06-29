package com.test

import androidx.recyclerview.widget.LinearLayoutManager
import com.test.adapter.RateListAdapter
import com.test.di.ActivityScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import javax.inject.Named

@Module
abstract class MainScreenModule {

    @Binds
    @ActivityScope
    abstract fun view(activity: MainScreenActivity): MainScreenContract.View

    @Binds
    @ActivityScope
    abstract fun presenter(presenter: MainScreenPresenter): MainScreenContract.Presenter

    @Binds
    @ActivityScope
    abstract fun interactor(interactor: MainScreenInteractor): MainScreenContract.Interactor

    @Module
    companion object {

        @Provides
        @ActivityScope
        @JvmStatic
        @Named("to")
        fun provideToRateListAdapter(): RateListAdapter =
            RateListAdapter()

        @Provides
        @ActivityScope
        @JvmStatic
        @Named("from")
        fun provideFromRateListAdapter(): RateListAdapter =
            RateListAdapter()

        @Provides
        @ActivityScope
        @JvmStatic
        fun providerLinearLayoutManager(context: MainScreenActivity): LinearLayoutManager {
            return LinearLayoutManager(context)
        }
    }
}