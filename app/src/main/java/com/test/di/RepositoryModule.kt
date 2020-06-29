package com.test.di

import com.test.data.ILocalRepository
import com.test.data.IRateRepository
import com.test.data.LocalRepository
import com.test.data.RateRepository
import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun provideChatRepository(repository: RateRepository): IRateRepository

    @Binds
    abstract fun provideContactRepository(repository: LocalRepository): ILocalRepository
}
