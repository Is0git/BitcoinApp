package com.is0git.bitcoinexchangecalculator.exchange_calculator

import android.util.Log
import com.is0git.bitcoinexchangecalculator.converter.converter_manager.ConverterManager
import com.is0git.bitcoinexchangecalculator.currencies_storage.CurrencyStorage
import com.is0git.bitcoinexchangecalculator.data.Currency
import com.is0git.bitcoinexchangecalculator.listeners.ExchangeListener
import kotlinx.coroutines.*

const val EXCHANGE_CALCULATOR_TAG = "EXCHANGE_CALCULATOR_TAG"

abstract class ExchangeCalculator<T : Currency> {

    var exchangeListener: ExchangeListener<T>? = null
    protected lateinit var currencyStorage: CurrencyStorage<T>
    protected lateinit var converterManager: ConverterManager<T>
    var conversionJobHandler = CoroutineExceptionHandler { coroutineContext, throwable ->
        converterManager.handleConversionException(throwable)
        exchangeListener?.onExchangeFailed(throwable)
    }

    suspend fun convertFromBase(value: String, key: String) {
        coroutineScope {
            val conversionJob  =  launch() {
                val currency = currencyStorage.getCurrency(key)
                val conversionResult = converterManager.convertFromBase(value, currency)
                withContext(Dispatchers.Main) { exchangeListener?.onExchangeComplete(conversionResult) }
            }
            onConversionCompleted(conversionJob)
        }
    }

    suspend fun convertToBase(value: String, key: String) {
        coroutineScope {
          val conversionJob  =  launch() {
                val currency = currencyStorage.getCurrency(key)
                val conversionResult = converterManager.convertToBase(value, currency)
                withContext(Dispatchers.Main) { exchangeListener?.onExchangeComplete(conversionResult) }
            }
            onConversionCompleted(conversionJob)
        }
    }

    private fun onConversionCompleted(job: Job) {
        job.invokeOnCompletion {
            Log.i(EXCHANGE_CALCULATOR_TAG, it?.message ?: "Conversion job is successful")
        }
    }

    fun updateCurrency(currency: T) {
        currencyStorage.updateCurrency(currency)
    }
}
