package com.example.maw9oot.data.remote

import com.example.maw9oot.data.remote.response.PrayerTimesResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface PrayerTimeAPI {
    @GET("v1/calendar/{year}")
    suspend fun getPrayerTimes(
        @Path("year") year: Int,
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double,
        @Query("annual") annual: Boolean = true,
        @Query("method") method: Int = 4
    ): PrayerTimesResponse
}
