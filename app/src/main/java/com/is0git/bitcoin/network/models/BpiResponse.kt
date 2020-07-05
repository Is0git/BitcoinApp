package com.is0git.bitcoin.network.models

import com.is0git.bitcoinexchangecalculator.data.BitcoinCurrency
import com.squareup.moshi.Json

data class BpiResponse(
    @field:Json(name = "chartName")
    val chartName: String? = null,
    @field:Json(name = "bpi")
    val bpi: Map<String, BitcoinCurrency>,
    @field:Json(name = "time")
    val time: Time? = null,
    @field:Json(name = "disclaimer")
    val disclaimer: String? = null
)