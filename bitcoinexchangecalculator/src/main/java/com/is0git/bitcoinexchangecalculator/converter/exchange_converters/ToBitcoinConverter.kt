package com.is0git.bitcoinexchangecalculator.converter.exchange_converters

import com.is0git.bitcoinexchangecalculator.data.BitcoinConversionResult
import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.is0git.bitcoinexchangecalculator.data.Currency
import java.math.RoundingMode

class ToBitcoinConverter :
    ExchangeConverter<BitcoinConversionResult> {
    override fun convert(valueFrom: String, currency: Currency): BitcoinConversionResult {
        (currency as BitcoinCurrency).apply {
            val valueFromBigDecimal = valueFrom.toFloat().toBigDecimal()
            val rateBigDecimal = currency.rateFloat?.toBigDecimal()
            val divided = valueFromBigDecimal.divide(rateBigDecimal,8,RoundingMode.HALF_UP)
            return BitcoinConversionResult(divided.toPlainString(), valueFrom, currency)
        }
    }
}