package com.is0git.bitcoinexchangecalculator.currencies_storage

import com.is0git.bitcoinexchangecalculator.data.Currency

abstract class CurrencyStorage<T : Currency> {

    protected val currencies: MutableMap<String, T> = mutableMapOf()

    fun updateCurrency(currency: T) {
        if (currency.code != null) currencies[currency.code] = currency
    }

    fun updateCurrencies(newCurrencies: Map<String, T>) {
        currencies.clear()
        currencies.putAll(newCurrencies)
    }

    /**
     * @param key usually a unique code to get currency from map
     */
    fun getCurrency(key: String): T {
        return currencies[key] ?: throw IllegalStateException("currency of this type doesn't exist in the currency storage")
    }
}