package com.akavi.weatheracode

import com.google.android.gms.common.api.internal.ApiKey
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface WeatherServices {
    @GET("weather")
    suspend fun getWeather(
        @Query("q") city: String,
        @Query("appid") apiKey: String): WeatherData
}


data class WeatherData(
    val main: Main,
    val weather: List<Weather>,
    val name: String
)

data class Main(val temp:Double, val feelsLike: Double)
data class Weather(val main: String, val description:String)