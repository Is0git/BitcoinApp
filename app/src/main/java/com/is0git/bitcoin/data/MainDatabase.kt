package com.is0git.bitcoin.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.is0git.bitcoin.data.dao.BpiDao
import com.is0git.bitcoin.data.entities.RoomBpi
import com.is0git.bitcoin.data.entities.RoomBpiData

@Database(entities = [RoomBpi::class, RoomBpiData::class], version = 3, exportSchema = true)
abstract class MainDatabase : RoomDatabase() {
    abstract fun getBpiDao() : BpiDao
}