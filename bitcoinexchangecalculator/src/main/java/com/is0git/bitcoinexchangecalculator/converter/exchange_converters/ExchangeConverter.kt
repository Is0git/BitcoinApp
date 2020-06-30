package com.is0git.bitcoinexchangecalculator.converter.exchange_converters

import com.is0git.bitcoinexchangecalculator.data.ConversionResult
import com.is0git.bitcoinexchangecalculator.data.Currency

interface ExchangeConverter<T : ConversionResult<out Currency>> {
    /**
     * @param currency takes currency object which contains essential data about currency
     * @return same currency object will be populated in
     * @see ConversionResult populated with conversion result in order to have extra data for the user
     */
    fun convert(valueFrom: String, currency: Currency) : T
}