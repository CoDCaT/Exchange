package com.test.api.model

import java.math.BigDecimal
import java.util.*


data class Rates(
    val name: String,
    val value: BigDecimal,
    var totalValue: Double = 100.00,
    var exchangeName: String = "",
    var exchangeValue: BigDecimal = BigDecimal(1),
    var forExchangeValue: Double = 0.0,
    var date: Date = Date(),
    var base: String = ""
)