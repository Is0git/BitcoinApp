package com.is0git.bitcoinexchangecalculator.converter

import android.util.Log
import com.is0git.bitcoinexchangecalculator.exchange_calculator.EXCHANGE_CALCULATOR_TAG
import java.lang.Exception

class ConversionExceptionHandler {
    fun handleConversionException(ex: Throwable) {
        when(ex) {
            is KotlinNullPointerException -> Log.e(EXCHANGE_CALCULATOR_TAG, "check if all necessary values were provided: ${ex.message}" )
            else -> Log.e(EXCHANGE_CALCULATOR_TAG, "undefined error: ${ex.message}")
        }
    }
}