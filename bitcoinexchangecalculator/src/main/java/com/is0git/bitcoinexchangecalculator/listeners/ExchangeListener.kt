package com.is0git.bitcoinexchangecalculator.listeners

import com.is0git.bitcoinexchangecalculator.data.ConversionResult
import com.is0git.bitcoinexchangecalculator.data.Currency

interface ExchangeListener<T : Currency> {
    fun onExchangeFailed(throwable: Throwable?)
    fun onExchangeComplete(conversionResult: ConversionResult<T>)
}
