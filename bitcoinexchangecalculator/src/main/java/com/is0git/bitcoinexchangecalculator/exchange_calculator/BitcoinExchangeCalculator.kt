package com.is0git.bitcoinexchangecalculator.exchange_calculator

import com.is0git.bitcoinexchangecalculator.converter.converter_manager.BitcoinConverterManager
import com.is0git.bitcoinexchangecalculator.currencies_storage.BitcoinCurrencyStorage
import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency

class BitcoinExchangeCalculator : ExchangeCalculator<BitcoinCurrency>() {
    init {
        converterManager =
            BitcoinConverterManager()
        currencyStorage = BitcoinCurrencyStorage()
    }

    /**
     * [code] is currency unique identifier
     */
    suspend fun convertFromBtc(value: String, code: String) {
        convertFromBase(value, code)
    }

    suspend fun convertToBtc(value: String, code: String) {
        convertToBase(value, code)
    }
}