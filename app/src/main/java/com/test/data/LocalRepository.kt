package com.test.data

import com.test.api.model.Rates
import com.test.room.RateValueRoom
import com.test.room.RoomDataBaseImpl
import com.test.room.UserValueRoom
import com.test.utils.ExchangeRateError
import com.test.utils.ExchangeResult
import com.test.utils.ExchangeSuccess
import com.test.utils.ExchangeTotalValueError
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject
import java.math.BigDecimal
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocalRepository @Inject constructor() : ILocalRepository {

    @Inject lateinit var dataBase: RoomDataBaseImpl
    @Inject lateinit var preference: IPreference

    private var currentValueFrom: Int = 0
    private var currentValueTo: Int = 0
    private var currentExchangeFrom: Double = 0.0
    private var currentExchangeTo: Double = 0.0
    private lateinit var rateListTo: MutableList<Rates>
    private lateinit var rateListFrom: MutableList<Rates>
    private val ratesSubject =
        BehaviorSubject.create<Pair<MutableList<Rates>, MutableList<Rates>>>()
    private val exchangeSubject =
        BehaviorSubject.create<Pair<MutableList<Rates>, MutableList<Rates>>>()


    override fun getNewRate(): BehaviorSubject<Pair<MutableList<Rates>, MutableList<Rates>>> =
        ratesSubject

    override fun getExchangeRate(): BehaviorSubject<Pair<MutableList<Rates>, MutableList<Rates>>> =
        exchangeSubject

    override fun saveRatesLists(list: MutableList<Rates>) {
        if (preference.isFirstRun()) firstInitData(list)
        val userValue = dataBase.userValueDao()
        list.map {
            it.totalValue = userValue.getUserValuesByName(it.name).value
        }
        rateListFrom = list.map {
            it.copy(
                name = it.name,
                value = it.value,
                totalValue = it.totalValue,
                exchangeName = it.exchangeName,
                exchangeValue = it.exchangeValue,
                forExchangeValue = it.forExchangeValue,
                date = it.date,
                base = it.base
            )
        }.toMutableList()
        rateListTo = list.map {
            it.copy(
                name = it.name,
                value = it.value,
                totalValue = it.totalValue,
                exchangeName = it.exchangeName,
                exchangeValue = it.exchangeValue,
                forExchangeValue = it.forExchangeValue,
                date = it.date,
                base = it.base
            )
        }.toMutableList()

        val rateValue = dataBase.rateValueDao()
        list.forEach {
            rateValue.insertOrUpdateVideoData(
                RateValueRoom(
                    name = it.name,
                    value = it.value.toString(),
                    baseValue = it.base,
                    date = it.date
                )
            )
        }
    }

    override fun calculateExchange() {
        rateListFrom[currentValueFrom].apply {
            exchangeName = rateListTo[currentValueTo].name
            exchangeValue = rateListTo[currentValueTo].value / value
            forExchangeValue =
                currentExchangeTo * (value.toDouble() / rateListTo[currentValueTo].value.toDouble())
        }
        rateListTo[currentValueTo].apply {
            exchangeName = rateListFrom[currentValueFrom].name
            exchangeValue = rateListFrom[currentValueFrom].value / value
            forExchangeValue =
                currentExchangeFrom * (value.toDouble() / rateListFrom[currentValueFrom].value.toDouble())
        }
    }

    override fun updateRateList() {
        ratesSubject.onNext(rateListFrom to rateListTo)
    }

    override fun updateExchange() {
        exchangeSubject.onNext(rateListFrom to rateListTo)
    }

    override fun saveCurrentRateFrom(position: Int) {
        currentExchangeFrom = 0.0
        currentExchangeTo = 0.0
        currentValueFrom = position
    }

    override fun saveCurrentRateTo(position: Int) {
        currentExchangeFrom = 0.0
        currentExchangeTo = 0.0
        currentValueTo = position
    }

    override fun saveCurrentExchangeFrom(value: Double) {
        currentExchangeFrom = value
        currentExchangeTo =
            currentExchangeFrom * (rateListTo[currentValueTo].value.toDouble() / rateListFrom[currentValueFrom].value.toDouble())
    }

    override fun saveCurrentExchangeTo(value: Double) {
        currentExchangeTo = value
        currentExchangeFrom =
            currentExchangeTo * (rateListFrom[currentValueFrom].value.toDouble() / rateListTo[currentValueTo].value.toDouble())
    }

    override fun getRatesListTo(): MutableList<Rates> =
        if (::rateListTo.isInitialized) rateListTo else getRates()

    override fun getRatesListFrom(): MutableList<Rates> =
        if (::rateListFrom.isInitialized) rateListFrom else getRates()

    private fun getRates(): MutableList<Rates> {
        val rateValue = dataBase.rateValueDao()
        val userValue = dataBase.userValueDao()
        return rateValue.getAllRateValues().map {
            Rates(
                name = it.name,
                value = BigDecimal(it.value),
                totalValue = userValue.getUserValuesByName(it.name).value
            )
        }.toMutableList()
    }

    private fun firstInitData(result: MutableList<Rates>) {
        val userValue = dataBase.userValueDao()
        result.forEach {
            userValue.insertOrUpdateVideoData(
                UserValueRoom(
                    name = it.name,
                    value = 100.00
                )
            )
        }
        preference.setIsFirstRun()
    }

    override fun clearCache() {
        //TODO: clear
    }

    override fun applyExchange(): Single<ExchangeResult> = Single.create { emitter ->

        if (currentValueFrom == currentValueTo) emitter.onSuccess(ExchangeRateError)

        rateListFrom[currentValueFrom].let {
            if (it.forExchangeValue > it.totalValue) {
                emitter.onSuccess(ExchangeTotalValueError)
            } else {
                it.totalValue -= it.forExchangeValue
                rateListTo[currentValueTo].apply {
                    totalValue += forExchangeValue
                }
                val userValue = dataBase.userValueDao()
                userValue.insertOrUpdateVideoData(
                    UserValueRoom(
                        name = it.name,
                        value = it.totalValue
                    )
                )
                userValue.insertOrUpdateVideoData(
                    UserValueRoom(
                        name = rateListTo[currentValueTo].name,
                        value = rateListTo[currentValueTo].totalValue
                    )
                )
                emitter.onSuccess(ExchangeSuccess)
            }
        }
    }
}