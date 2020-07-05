package com.is0git.bitcoin.di.modules

import com.is0git.bitcoinexchangecalculator.exchange_calculator.BitcoinExchangeCalculator
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent

@Module
@InstallIn(ActivityRetainedComponent::class)
object BitCoinExchangeModule {
    @Provides
    @JvmStatic
    fun getBitCoinExchanger(): BitcoinExchangeCalculator = BitcoinExchangeCalculator()
}