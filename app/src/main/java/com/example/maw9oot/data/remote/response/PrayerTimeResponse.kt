package com.example.maw9oot.data.remote.response

data class PrayerTimesResponse(
    val code: Int,
    val status: String,
    val data: Map<String, List<PrayerData>> // Map with string keys and list of PrayerData values
)

data class PrayerData(
    val timings: Timings,
    val date: DateDetails,
    val meta: Meta
)

data class Timings(
    val Fajr: String,
    val Sunrise: String,
    val Dhuhr: String,
    val Asr: String,
    val Sunset: String,
    val Maghrib: String,
    val Isha: String,
    val Imsak: String,
    val Midnight: String,
    val Firstthird: String,
    val Lastthird: String
)

data class DateDetails(
    val readable: String,
    val timestamp: String,
    val gregorian: GregorianDate,
    val hijri: HijriDate
)

data class GregorianDate(
    val date: String,
    val format: String,
    val day: String,
    val weekday: Weekday,
    val month: Month,
    val year: String,
    val designation: Designation
)

data class HijriDate(
    val date: String,
    val format: String,
    val day: String,
    val weekday: Weekday,
    val month: Month,
    val year: String,
    val designation: Designation,
    val holidays: List<String>
)

data class Weekday(
    val en: String,
    val ar: String? = null
)

data class Month(
    val number: Int,
    val en: String,
    val ar: String? = null
)

data class Designation(
    val abbreviated: String,
    val expanded: String
)

data class Meta(
    val latitude: Double,
    val longitude: Double,
    val timezone: String,
    val method: Method,
    val latitudeAdjustmentMethod: String,
    val midnightMode: String,
    val school: String,
    val offset: Offset
)

data class Method(
    val id: Int,
    val name: String,
    val params: Params,
    val location: Location
)

data class Params(
    val Fajr: Double, // Use Double for numerical values
    val Isha: String  // Use String for values with units
)


data class Location(
    val latitude: Double,
    val longitude: Double
)

data class Offset(
    val Imsak: Int,
    val Fajr: Int,
    val Sunrise: Int,
    val Dhuhr: Int,
    val Asr: Int,
    val Maghrib: Int,
    val Sunset: Int,
    val Isha: Int,
    val Midnight: Int
)
