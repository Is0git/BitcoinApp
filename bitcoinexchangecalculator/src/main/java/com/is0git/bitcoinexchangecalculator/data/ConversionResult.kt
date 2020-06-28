package com.is0git.bitcoinexchangecalculator.data

abstract class ConversionResult<T : Currency>(var convertedResult: String, var convertedFrom: String, currency: T){
}