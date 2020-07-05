package com.is0git.bitcoin.network.adapters

import com.is0git.bitcoin.network.models.BpiData
import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.squareup.moshi.FromJson
import dagger.hilt.android.scopes.ActivityRetainedScoped
import javax.inject.Inject

@ActivityRetainedScoped
class BpiAdapter @Inject constructor() {
    @FromJson
    fun fromJson(bpiData: BpiData): BitcoinCurrency {
        return BitcoinCurrency(bpiData.code, bpiData.rate, bpiData.rateFloat, bpiData.description, bpiData.symbol)
    }
}