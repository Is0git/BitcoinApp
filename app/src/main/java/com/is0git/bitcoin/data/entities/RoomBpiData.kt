package com.is0git.bitcoin.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bpi_data")
data class RoomBpiData(
    val chartName: String?,
    val symbol: String?,
    val rateFloat: Float?,
    @PrimaryKey(autoGenerate = false)
    val code: String,
    val rate: String?,
    val description: String?
)