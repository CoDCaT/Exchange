package com.test.utils

sealed class ExchangeResult

object ExchangeSuccess : ExchangeResult()
object ExchangeRateError : ExchangeResult()
object ExchangeTotalValueError : ExchangeResult()