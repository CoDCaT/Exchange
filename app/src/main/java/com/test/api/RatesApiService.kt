package com.test.api

import io.reactivex.Observable
import okhttp3.ResponseBody
import retrofit2.http.GET

interface RatesApiService {

    @GET("latest")
    fun getExchangeRates(): Observable<ResponseBody>
}