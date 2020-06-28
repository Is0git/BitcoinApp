package com.is0git.bitcoinexchangecalculator.converter.converter_manager

import com.is0git.bitcoinexchangecalculator.converter.ConversionExceptionHandler
import com.is0git.bitcoinexchangecalculator.converter.exchange_converters.ExchangeConverter
import com.is0git.bitcoinexchangecalculator.data.Currency
import com.is0git.bitcoinexchangecalculator.data.ConversionResult

abstract class ConverterManager<T : Currency> {

    protected lateinit var exchangeConverter: ExchangeConverter<out ConversionResult<T>>
    protected lateinit var reverseExchangeConverter: ExchangeConverter<out ConversionResult<T>>
    private val conversionExceptionHandler = ConversionExceptionHandler()

    fun convertFromBase(valueFrom: String, currency: T) : ConversionResult<T> {
        return exchangeConverter.convert(valueFrom, currency)
    }

    fun convertToBase(valueFrom: String, currency: T) : ConversionResult<T> {
        return reverseExchangeConverter.convert(valueFrom, currency)
    }
    fun handleConversionException(throwable: Throwable) {
        conversionExceptionHandler.handleConversionException(throwable)
    }

}