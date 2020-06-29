package com.test

import com.test.api.model.Rates
import com.test.data.ILocalRepository
import com.test.data.IRateRepository
import com.test.di.ActivityScope
import com.test.utils.ExchangeResult
import com.test.utils.ExchangeSuccess
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import javax.inject.Inject

@ActivityScope
class MainScreenInteractor @Inject constructor() : MainScreenContract.Interactor {

    @Inject
    lateinit var ratesRepository: IRateRepository

    @Inject
    lateinit var localRepository: ILocalRepository


    override fun connectUpdateRateList(): Observable<Pair<MutableList<Rates>, MutableList<Rates>>> =
        localRepository.getNewRate()

    override fun connectUpdateExchange(): Observable<Pair<MutableList<Rates>, MutableList<Rates>>> =
        localRepository.getExchangeRate()

    override fun updateRatesValue(): Flowable<Pair<MutableList<Rates>, MutableList<Rates>>> =
        ratesRepository
            .getLastRates()
            .doOnNext {
                localRepository.saveRatesLists(it)
                localRepository.calculateExchange()
                localRepository.updateRateList()
            }
            .map {
                localRepository.getRatesListFrom() to localRepository.getRatesListTo()
            }

    override fun changeValueFrom(position: Int): Completable = Completable.fromCallable {
        localRepository.saveCurrentRateFrom(position)
        localRepository.calculateExchange()
        localRepository.updateExchange()
    }

    override fun changeValueTo(position: Int): Completable = Completable.fromCallable {
        localRepository.saveCurrentRateTo(position)
        localRepository.calculateExchange()
        localRepository.updateExchange()
    }

    override fun changeExchangeFrom(value: Double): Single<MutableList<Rates>> =
        Single.fromCallable {
            localRepository.saveCurrentExchangeFrom(value)
            localRepository.calculateExchange()
            localRepository.getRatesListTo()
        }

    override fun changeExchangeTo(value: Double): Single<MutableList<Rates>> = Single.fromCallable {
        localRepository.saveCurrentExchangeTo(value)
        localRepository.calculateExchange()
        localRepository.getRatesListFrom()
    }

    override fun clearCache(): Completable = Completable.fromCallable {
        localRepository.clearCache()
    }

    override fun exchange(): Single<ExchangeResult> =
        localRepository
            .applyExchange()
            .doOnSuccess { result ->
                if (result is ExchangeSuccess) localRepository.updateExchange()
            }

    override fun calculateExchange(
        valueFrom: Int,
        valueTo: Int
    ): Observable<Pair<MutableList<Rates>, MutableList<Rates>>> = Observable.fromCallable {
        val ratesFrom = localRepository.getRatesListFrom()
        val ratesTo = localRepository.getRatesListTo()

        ratesFrom to ratesTo
    }

    override fun getRateListTo(): Single<MutableList<Rates>> = Single.fromCallable {
        localRepository.getRatesListTo()
    }

    override fun getRateListFrom(): Single<MutableList<Rates>> = Single.fromCallable {
        localRepository.getRatesListFrom()
    }
}