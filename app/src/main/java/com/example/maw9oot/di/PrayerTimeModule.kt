package com.example.maw9oot.di

import com.example.maw9oot.data.local.PrayerTimeDao
import com.example.maw9oot.data.remote.PrayerTimeAPI
import com.example.maw9oot.data.repository.PrayerTimesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object PrayerTimeModule {

    @Provides
    @Singleton
    fun providePrayerTimesAPI(): PrayerTimeAPI {
        return Retrofit.Builder()
            .baseUrl("https://api.aladhan.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(PrayerTimeAPI::class.java)
    }

    @Provides
    @Singleton
    fun providePrayerTimeRepository(prayerTimeAPI: PrayerTimeAPI, prayerTimeDao: PrayerTimeDao): PrayerTimesRepository {
        return PrayerTimesRepository(prayerTimeAPI, prayerTimeDao)
    }
}