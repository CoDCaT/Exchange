package com.test

import com.test.api.model.Rates
import com.test.api.model.RatesResult
import com.test.utils.ExchangeResult
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.subjects.BehaviorSubject

interface MainScreenContract {

    interface View {
        fun setFromRateList(rateList: List<Rates>)
        fun setToRateList(rateList: List<Rates>)
        fun showProgress(show: Boolean)
        fun updateFromRates(fromList: MutableList<Rates>)
        fun updateToRates(toList: MutableList<Rates>)
        fun showSuccessState()
        fun showEqualsRatesState()
        fun showTotalValueState()
    }

    interface Presenter {
        fun onViewInitialized()
        fun onViewDestroyed()
        fun onValueFromChanged(position: Int)
        fun onValueToChanged(position: Int)
        fun onExchangeValueChangedFrom(value: Double)
        fun onExchangeValueChangedTo(value: Double)
        fun onExchangeConfirm()
    }

    interface Interactor {
        fun updateRatesValue(): Flowable<Pair<MutableList<Rates>, MutableList<Rates>>>
        fun calculateExchange(valueFrom: Int, valueTo: Int): Observable<Pair<MutableList<Rates>, MutableList<Rates>>>
        fun getRateListTo(): Single<MutableList<Rates>>
        fun getRateListFrom(): Single<MutableList<Rates>>
        fun connectUpdateRateList(): Observable<Pair<MutableList<Rates>, MutableList<Rates>>>
        fun connectUpdateExchange(): Observable<Pair<MutableList<Rates>, MutableList<Rates>>>
        fun changeValueFrom(position: Int): Completable
        fun changeValueTo(position: Int): Completable
        fun changeExchangeFrom(value: Double): Single<MutableList<Rates>>
        fun changeExchangeTo(value: Double): Single<MutableList<Rates>>
        fun clearCache(): Completable
        fun exchange(): Single<ExchangeResult>
    }
}