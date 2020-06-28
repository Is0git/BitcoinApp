package com.is0git.bitcoinexchangecalculator.converter.exchange_converters

import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.is0git.bitcoinexchangecalculator.data.BitcoinConversionResult
import com.is0git.bitcoinexchangecalculator.data.Currency
import org.joda.money.Money
import java.math.BigDecimal
import java.math.RoundingMode

class ToBitcoinConverter :
    ExchangeConverter<BitcoinConversionResult> {
    override fun convert(valueFrom: String, currency: Currency): BitcoinConversionResult {
        (currency as BitcoinCurrency).apply {
            val valueFromBigDecimal = BigDecimal(valueFrom)
            val rateBigDecimal = BigDecimal(currency.rate)
            val divided = valueFromBigDecimal.divide(rateBigDecimal, BigDecimal.ROUND_DOWN)
            return BitcoinConversionResult(divided.toPlainString(), valueFrom, currency)
        }
    }
}