package com.example.maw9oot.di

import android.content.Context
import androidx.room.Room
import com.example.maw9oot.data.local.PrayerLogDatabase
import com.example.maw9oot.data.local.PrayerLogDao
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
    fun provideDatabase(@ApplicationContext appContext : Context) : PrayerLogDatabase {
        return Room.databaseBuilder(
            appContext,
            PrayerLogDatabase::class.java,
            "maw9oot_database"
        ).build()
    }

    @Provides
    @Singleton
    fun providePrayerLogDao(database: PrayerLogDatabase) : PrayerLogDao {
        return database.prayerLogDao()
    }

    @Provides
    @Singleton
    fun providePrayerLogRepository(prayerLogDao: PrayerLogDao): PrayerLogRepository {
        return PrayerLogRepository(prayerLogDao)
    }
}