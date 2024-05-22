package com.akavi.weatheracode

import android.app.Application
import android.location.Location
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeatherViewModel(application:Application): AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private var fusedLocationClient: FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    private var _latitude = MutableStateFlow(0.0)
    val latitude = _latitude.asStateFlow()

    private var _longitude = MutableStateFlow(0.0)
    val longitude = _longitude.asStateFlow()

    fun obtainLocation(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location:Location? ->
                _latitude.value = location?.latitude!!
                _longitude.value = location.longitude
            }

    }

}