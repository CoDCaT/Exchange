package com.test.data

import com.test.api.model.Rates
import com.test.api.model.RatesResult
import com.test.utils.ExchangeResult
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface ILocalRepository {
    fun getRatesListTo(): MutableList<Rates>
    fun getRatesListFrom(): MutableList<Rates>
    fun getNewRate(): BehaviorSubject<Pair<MutableList<Rates>, MutableList<Rates>>>
    fun getExchangeRate(): BehaviorSubject<Pair<MutableList<Rates>, MutableList<Rates>>>
    fun saveRatesLists(list: MutableList<Rates>)
    fun calculateExchange()
    fun saveCurrentRateFrom(position: Int)
    fun saveCurrentRateTo(position: Int)
    fun saveCurrentExchangeFrom(value: Double)
    fun saveCurrentExchangeTo(value: Double)
    fun updateRateList()
    fun updateExchange()
    fun clearCache()
    fun applyExchange(): Single<ExchangeResult>
}