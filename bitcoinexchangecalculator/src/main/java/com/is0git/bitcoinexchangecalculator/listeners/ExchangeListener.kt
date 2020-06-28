package com.is0git.bitcoinexchangecalculator.listeners

import com.is0git.bitcoinexchangecalculator.data.Currency
import com.is0git.bitcoinexchangecalculator.data.ConversionResult

interface ExchangeListener<T : Currency> {
    fun onExchangeFailed(throwable: Throwable?)
    fun onExchangeComplete(currencyData: ConversionResult<T>)
}
