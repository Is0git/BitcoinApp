package com.is0git.bitcoin.data.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.is0git.bitcoin.network.models.Time

@Entity(tableName = "bpi_table")
data class RoomBpi(
    @PrimaryKey(autoGenerate = false)
    val chartName: String,
    val disclaimer: String?, @Embedded
    val time: Time?,
    var lastUpdatedMs: Long
)
