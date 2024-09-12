package com.example.maw9oot.di

import android.content.Context
import androidx.room.Room
import com.example.maw9oot.data.local.DataStoreManager
import com.example.maw9oot.data.local.PrayerDatabase
import com.example.maw9oot.data.local.PrayerLogDao
import com.example.maw9oot.data.local.PrayerTimeDao
import com.example.maw9oot.data.repository.PrayerLogRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext appContext : Context) : PrayerDatabase {
        return Room.databaseBuilder(
            appContext,
            PrayerDatabase::class.java,
            "maw9oot_db"
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerLogDao(database: PrayerDatabase) : PrayerLogDao {
        return database.prayerLogDao()
    }

    @Provides
    @Singleton
    fun providePrayerTimeDao(database: PrayerDatabase) : PrayerTimeDao {
        return database.prayerTimeDao()
    }

    @Provides
    @Singleton
    fun providePrayerLogRepository(prayerDatabase: PrayerDatabase): PrayerLogRepository {
        return PrayerLogRepository(prayerDatabase)
    }
}