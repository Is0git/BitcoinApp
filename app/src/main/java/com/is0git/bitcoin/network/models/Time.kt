package com.is0git.bitcoin.network.models

import com.squareup.moshi.Json

data class Time(
    @field:Json(name = "updateduk")
    val updateduk: String? = null,
    @field:Json(name = "updatedISO")
    val updatedISO: String? = null,
    @field:Json(name = "updated")
    val updated: String? = null
)