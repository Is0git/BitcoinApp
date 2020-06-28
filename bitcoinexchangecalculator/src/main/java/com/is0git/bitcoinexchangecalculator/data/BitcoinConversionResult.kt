package com.is0git.bitcoinexchangecalculator.data

class BitcoinConversionResult(conversionResult: String, convertedFrom: String, currency: BitcoinCurrency) : ConversionResult<BitcoinCurrency>(conversionResult, convertedFrom, currency) {
}