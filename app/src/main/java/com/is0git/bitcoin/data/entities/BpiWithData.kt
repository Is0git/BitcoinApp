package com.is0git.bitcoin.data.entities

import androidx.room.Embedded
import androidx.room.Relation

data class BpiWithData(
    @Embedded val bpi: RoomBpi,
    @Relation(parentColumn = "chartName", entityColumn = "chartName")
    val bpiData: List<RoomBpiData>
) {

}