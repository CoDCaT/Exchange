package com.test.data

import com.test.api.model.Rates
import io.reactivex.Flowable

interface IRateRepository {

    fun getLastRates(): Flowable<MutableList<Rates>>
}