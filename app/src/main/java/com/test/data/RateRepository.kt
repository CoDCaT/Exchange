package com.test.data

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.test.utils.REQUEST_DELAY
import com.test.api.RatesApiService
import com.test.api.model.Rates
import io.reactivex.Flowable
import java.math.BigDecimal
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RateRepository @Inject constructor(private val rateApiService: RatesApiService) : IRateRepository {

    override fun getLastRates(): Flowable<MutableList<Rates>> =
        rateApiService
            .getExchangeRates().singleOrError()
            .map { mapRateResult(it.string()) }
            .repeatWhen { it.delay(REQUEST_DELAY, TimeUnit.MILLISECONDS) }

    private fun mapRateResult(rateResult: String): MutableList<Rates> {
        val json: JsonObject = JsonParser().parse(rateResult) as JsonObject
        val rates = (json.get("rates") as JsonObject).entrySet().map {
            Rates(name = it.key, value = BigDecimal.valueOf(it.value.asDouble))
        }.toMutableList()

        rates.add(
            Rates(
                name = json.get("base").asString,
                value = BigDecimal.valueOf(1.00),
                date = formatDate(json.get("date").asString),
                base = json.get("base").asString
            )
        )
        return rates
    }

    private fun formatDate(date: String): Date {
        val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return format.parse(date) ?: Date()
    }

    companion object {
        private const val MOCK =
            "{\"rates\":{\"CAD\":1.5209,\"HKD\":8.6889,\"ISK\":154.6,\"PHP\":56.133,\"DKK\":7.4554,\"HUF\":345.44,\"CZK\":26.683,\"AUD\":1.6261,\"RON\":4.8428,\"SEK\":10.567,\"IDR\":15927.67,\"INR\":85.352,\"BRL\":6.0029,\"RUB\":77.6565,\"HRK\":7.5665,\"JPY\":119.77,\"THB\":34.765,\"CHF\":1.0656,\"SGD\":1.5623,\"PLN\":4.4516,\"BGN\":1.9558,\"TRY\":7.6887,\"CNY\":7.9332,\"NOK\":10.7135,\"NZD\":1.7403,\"ZAR\":19.444,\"USD\":1.121,\"MXN\":25.3126,\"ILS\":3.862,\"GBP\":0.90505,\"KRW\":1353.53,\"MYR\":4.7854},\"base\":\"EUR\",\"date\":\"2020-06-19\"}"
    }
}