package com.test.api.model

import java.util.*

data class RatesResult(
    val rates: MutableList<Rates>,
    val base: String,
    val date: Date
)