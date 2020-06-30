package com.is0git.bitcoinexchangecalculator.data

class BitcoinCurrency(code: String?, rate: String?, rateFloat: Float?, val description: String?, val symbol: String?) : Currency(code, rate, rateFloat) {
}