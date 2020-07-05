package com.is0git.bitcoin.data.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import com.is0git.bitcoin.data.entities.BpiWithData
import com.is0git.bitcoin.data.entities.RoomBpi
import com.is0git.bitcoin.data.entities.RoomBpiData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@Dao
abstract class BpiDao {
    suspend fun insertBpiWithData(bpiWithData: BpiWithData) {
        coroutineScope {
            launch(Dispatchers.IO) {
                insertBpi(bpiWithData.bpi)
            }
            insertBpiData(bpiWithData.bpiData)
        }
    }

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBpi(bpi: RoomBpi)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    abstract suspend fun insertBpiData(bpi: List<RoomBpiData>)

    @Transaction
    @Query("SELECT * FROM bpi_table WHERE chartName == :mChartName")
    abstract fun getBpiWithDataLiveData(mChartName: String): LiveData<BpiWithData?>

    @Query("SELECT lastUpdatedMs FROM bpi_table WHERE chartName == :mChartName")
    abstract suspend fun getLastUpdatedTime(mChartName: String): Long?
}