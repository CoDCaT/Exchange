package com.test

import com.test.di.ActivityScope
import com.test.utils.ExchangeRateError
import com.test.utils.ExchangeSuccess
import com.test.utils.ExchangeTotalValueError
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.rxkotlin.addTo
import io.reactivex.schedulers.Schedulers
import timber.log.Timber
import javax.inject.Inject

@ActivityScope
class MainScreenPresenter @Inject constructor() : MainScreenContract.Presenter {

    private var compositeDisposable = CompositeDisposable()
    private var changeFromDisposable: Disposable? = null
    private var changeToDisposable: Disposable? = null

    @Inject
    lateinit var view: MainScreenContract.View

    @Inject
    lateinit var interactor: MainScreenContract.Interactor

    override fun onViewInitialized() {

        interactor
            .connectUpdateRateList()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext { view.showProgress(false) }
            .subscribe({ (fromList, toList) ->
                view.setFromRateList(fromList)
                view.setToRateList(toList)
                Timber.d("!!!!! TEST updateRatesValue fromList = $fromList toList = $toList")
            }, Timber::e)
            .addTo(compositeDisposable)

        interactor
            .connectUpdateExchange()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ (fromList, toList) ->
                view.updateFromRates(fromList)
                view.updateToRates(toList)
            }, Timber::e)
            .addTo(compositeDisposable)


        interactor
            .updateRatesValue()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { view.showProgress(true) }
            .doOnError { view.showProgress(false) }
            .subscribe({ Timber.d("Update rates...") }, Timber::e)
            .addTo(compositeDisposable)
    }

    override fun onViewDestroyed() {
        interactor.clearCache()
        compositeDisposable.dispose()
    }

    override fun onValueFromChanged(position: Int) {
        changeFromDisposable?.dispose()
        changeFromDisposable = interactor
            .changeValueFrom(position)
            .subscribeOn(Schedulers.single())
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onValueToChanged(position: Int) {
        changeToDisposable?.dispose()
        changeToDisposable = interactor
            .changeValueTo(position)
            .subscribeOn(Schedulers.single())
            .subscribe()
            .addTo(compositeDisposable)
    }

    override fun onExchangeValueChangedFrom(value: Double) {
        changeToDisposable?.dispose()
        changeToDisposable = interactor
            .changeExchangeFrom(value)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::updateToRates, Timber::e)
            .addTo(compositeDisposable)
    }

    override fun onExchangeValueChangedTo(value: Double) {
        changeToDisposable?.dispose()
        changeToDisposable = interactor
            .changeExchangeTo(value)
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(view::updateFromRates, Timber::e)
            .addTo(compositeDisposable)
    }

    override fun onExchangeConfirm() {
        interactor
            .exchange()
            .subscribeOn(Schedulers.single())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe({ result ->
                when(result){
                    ExchangeSuccess -> view.showSuccessState()
                    ExchangeRateError -> view.showEqualsRatesState()
                    ExchangeTotalValueError -> view.showTotalValueState()
                }
            }, Timber::e)
            .addTo(compositeDisposable)
    }
}