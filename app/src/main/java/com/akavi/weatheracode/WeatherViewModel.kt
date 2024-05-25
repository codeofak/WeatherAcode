package com.akavi.weatheracode

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class WeatherViewModel(application: Application) : AndroidViewModel(application) {

    private val context = getApplication<Application>()

    private var fusedLocationClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private var _latitude = MutableStateFlow(0.0)
    val latitude = _latitude.asStateFlow()

    private var _longitude: MutableStateFlow<Double> = MutableStateFlow(0.0)
    val longitude = _longitude.asStateFlow()

    @SuppressLint("MissingPermission")
    fun obtainLocation() {


        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                _latitude.value = location?.latitude!!
                _longitude.value = location.longitude
            }


    }

    fun isOnline(): Boolean{
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val compatibilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return compatibilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
    }

    fun checkLocationPermission(): Boolean{
        val fineLocationPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        val coarseLocationPermission = ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_COARSE_LOCATION
        )
        return fineLocationPermission == PackageManager.PERMISSION_GRANTED ||
                coarseLocationPermission == PackageManager.PERMISSION_GRANTED
    }

    fun requestLocationPermission(activity: Activity){
        ActivityCompat.requestPermissions(
            activity,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ),
            REQUEST_PERMISSIONS_CODE
        )
    }

}