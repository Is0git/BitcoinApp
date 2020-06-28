package com.is0git.bitcoinexchangecalculator.converter.converter_manager

import com.is0git.bitcoinexchangecalculator.converter.exchange_converters.FromBitcoinConverter
import com.is0git.bitcoinexchangecalculator.converter.exchange_converters.ToBitcoinConverter
import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency

class BitcoinConverterManager : ConverterManager<BitcoinCurrency>(){
    init {
        exchangeConverter = FromBitcoinConverter()
        reverseExchangeConverter = ToBitcoinConverter()
    }
}