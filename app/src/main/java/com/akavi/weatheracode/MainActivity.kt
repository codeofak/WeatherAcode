package com.akavi.weatheracode

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.VerticalAlignmentLine
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat
import com.akavi.weatheracode.ui.theme.WeatherAcodeTheme
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.math.roundToInt
import kotlin.math.roundToLong


const val api_id1 = "c0ac7f978b0401817a100f56a64a3878"
class MainActivity : ComponentActivity() {



    private lateinit var fusedLocationClient: FusedLocationProviderClient

    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val city = "Lucknow"
        val apiKey = api_id1




        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
       // Log.e("lat", weather_url1)

        setContent {


            WeatherAcodeTheme {

                val coroutineScope = rememberCoroutineScope()
                var temp by remember { mutableStateOf("") }
                var name by remember { mutableStateOf("") }
                coroutineScope.launch(Dispatchers.IO) {
                    //using retrofit
                    val weatherServices = WeatherClient.getWeatherServices()

                    val response = weatherServices.getWeather(city, apiKey)
                    temp = response.main.temp.minus(273.00).times(100.00).roundToInt().div(100.0).toString()
                    name = response.name
                }
                Column(modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = temp)
                    Text(text = name)
                }


            }
        }
    }


    private fun obtainLocation() {
        Log.e("lat", "function")

        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location: Location? ->
//                    weather_url1 =
//                        "http://api.weatherapi.com/v1/current.json?key=$api_id1&q=${location?.latitude},${location?.longitude}"
//                    Log.e("lat", weather_url1.toString())
                    //getTemp()
                }
            return
        }

    }

//    fun getTemp(): String {
//        //Instantiate the request queue
//        val queue = Volley.newRequestQueue(this)
//        //val url: String = weather_url1
//        //Log.e("lat", url)
//
//        var temperatureOfCity =""
//
//        //Request a string response
//        //from the provided url
//        val stringReq = StringRequest(
//            Request.Method.GET, url,
//            { response ->
//                Log.e("lat", response.toString())
//
//                //get the JSON object
//                val obj = JSONObject(response)
//
//                //get the array from obj of name - "data"
//                val arr = obj.getJSONArray("data")
//                Log.e("lat obj1", arr.toString())
//
//                //get JSON object from the
//                //array at index position 0
//                val obj2 = arr.getJSONObject(0)
//                Log.e("lat obj2", obj2.toString())
//
//                //set the temperature and the city
//                //name using getString() function
//                cityName = obj2.getString("name")
//                cityTemp = obj2.getString("temp_c")
//                temperatureOfCity = obj2.getString("temp_c")
//
//                Log.e("tempo",obj2.getString("temp_c"))
//            },
//            {
//                cityTemp = "Error"
//                cityName = "Error"
//            }
//        )
//        queue.add(stringReq)
//        return temperatureOfCity
//    }
}




