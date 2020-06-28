package com.is0git.bitcoinexchangecalculator

import android.util.Log
import com.is0git.bitcoinexchangecalculator.data.BitcoinConversionResult
import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.is0git.bitcoinexchangecalculator.data.ConversionResult
import com.is0git.bitcoinexchangecalculator.exchange_calculator.BitcoinExchangeCalculator
import com.is0git.bitcoinexchangecalculator.exchange_calculator.EXCHANGE_CALCULATOR_TAG
import com.is0git.bitcoinexchangecalculator.listeners.ExchangeListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import org.junit.Test

import org.junit.Assert.*


class ConversionTest {

    private val exchangeCalculator = BitcoinExchangeCalculator().apply {
        this.updateCurrency(BitcoinCurrency("EUR", "8160.1718", "desc", "&euro"))
        this.exchangeListener = object : ExchangeListener<BitcoinCurrency> {
            override fun onExchangeFailed(throwable: Throwable?) {
              Log.d(EXCHANGE_CALCULATOR_TAG, "message: $throwable")
            }

            override fun onExchangeComplete(currencyData: ConversionResult<BitcoinCurrency>) {
                currencyResult = currencyData
                Log.d(EXCHANGE_CALCULATOR_TAG, "completed: ${currencyData.convertedResult}")
            }

        }
    }

    var currencyResult: ConversionResult<BitcoinCurrency>? = null
    @Test
    fun fromBaseBitcoinConversion() = runBlocking {
        exchangeCalculator.convertFromBtc("0.00061", "EUR")
        assertEquals("40,749", currencyResult?.convertedResult)
    }

    @Test fun toBaseBitcoinConversion() = runBlocking {
        exchangeCalculator.convertToBtc("5", "EUR")
        assertEquals("5", currencyResult?.convertedResult)
    }

}
