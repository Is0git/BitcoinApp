package com.is0git.bitcoinexchangecalculator.converter.exchange_converters

import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.is0git.bitcoinexchangecalculator.data.BitcoinConversionResult
import com.is0git.bitcoinexchangecalculator.data.Currency
import org.joda.money.Money
import java.math.BigDecimal
import java.math.RoundingMode

class FromBitcoinConverter :
    ExchangeConverter<BitcoinConversionResult> {
    override fun convert(valueFrom: String, currency: Currency): BitcoinConversionResult {
        (currency as BitcoinCurrency).apply {
            val valueFromBigDecimal = BigDecimal(valueFrom)
            val rateBigDecimal = BigDecimal(currency.rate)
            val multiplied = valueFromBigDecimal.multiply(rateBigDecimal)
            return BitcoinConversionResult(multiplied.toPlainString(), valueFrom, currency)
        }
    }
}