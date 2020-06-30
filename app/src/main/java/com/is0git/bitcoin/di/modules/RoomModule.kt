package com.is0git.bitcoin.di.modules

import android.content.Context
import androidx.room.Room
import com.is0git.bitcoin.data.MainDatabase
import com.is0git.bitcoin.data.dao.BpiDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object RoomModule {

    @Provides
    @Singleton
    @Synchronized
    fun getMainDatabase(@ApplicationContext context: Context) : MainDatabase {
       return Room.databaseBuilder(context, MainDatabase::class.java, "main_database").fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun bpiDao(mainDatabase: MainDatabase) : BpiDao {
        return mainDatabase.getBpiDao()
    }
}