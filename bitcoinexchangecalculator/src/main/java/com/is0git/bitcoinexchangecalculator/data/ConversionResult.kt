package com.is0git.bitcoinexchangecalculator.data

abstract class ConversionResult<T : Currency>(var convertedResult: String, var convertedFrom: String, var currency: T, var flags: Int = 0){
    companion object {
        /**
         * If enabled, means that conversion was done to base currency instead of from base currency
         */
        const val FLAG_REVERSE_CONVERSION: Int = 1
    }
}