package com.is0git.bitcoin.network.models

import com.squareup.moshi.Json

data class BpiData(
    val chartName: Int,
    @field:Json(name = "symbol")
    val symbol: String,
    @field:Json(name = "rate_float")
    val rateFloat: Float,
    @field:Json(name = "code")
    val code: String,
    @field:Json(name = "rate")
    val rate: String,
    @field:Json(name = "description")
    val description: String
)