package com.akavi.weatheracode

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object WeatherClient {

    private const val BASE_URL = "https://api.openweathermap.org/data/2.5/"

    fun getRetrofitInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getWeatherServices(): WeatherServices {
        return getRetrofitInstance().create(WeatherServices::class.java)
    }
}