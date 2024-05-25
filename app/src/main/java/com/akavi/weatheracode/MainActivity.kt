package com.akavi.weatheracode

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.akavi.weatheracode.ui.theme.WeatherAcodeTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.Normalizer
import kotlin.math.roundToInt

const val REQUEST_PERMISSIONS_CODE = 100
const val api_id1 = "c0ac7f978b0401817a100f56a64a3878"

class MainActivity : ComponentActivity() {
    @SuppressLint("CoroutineCreationDuringComposition")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val city = "Lucknow"
        val apiKey = api_id1

        setContent {

            WeatherAcodeTheme {


                val weatherViewModel: WeatherViewModel by viewModels()

                if (weatherViewModel.isOnline()) {
                    App(weatherViewModel = weatherViewModel)
                } else {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "Internet Not Connected")
                    }
                }
            }
        }
    }


}


fun CharSequence.unaccent(): String {
    val REGEX_UNACCENT = "\\p{InCombiningDiacriticalMarks}+".toRegex()
    val temp = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.replace(temp, "")
}


@SuppressLint("CoroutineCreationDuringComposition")
@Composable
fun App(weatherViewModel: WeatherViewModel) {


    weatherViewModel.obtainLocation()

    //co-ordinates from viewmodel
    var latitude = weatherViewModel.latitude.collectAsState()
    var longitude = weatherViewModel.longitude.collectAsState()

    var latitudeTwoDecimal = latitude.value.times(100.00).roundToInt().div(100.0)
        .toString()
    var longitudeTwoDecimal = longitude.value.times(100.00).roundToInt().div(100.0)
        .toString()

    //Latitude Co-ordinates to DMS format
    var latitudeDegree = latitude.value.toInt()
    var latitudeMinute = latitude.value.minus(latitudeDegree).times(60)
    var latitudeSec = latitudeMinute.minus(latitudeMinute.toInt()).times(60)

    //Longitude Co-ordinates to DMS format
    var longitudeDegree = longitude.value.toInt()
    var longitudeMinute = longitude.value.minus(longitudeDegree).times(60)
    var longitudeSec = longitudeMinute.minus(longitudeMinute.toInt()).times(60)

    val coroutineScope = rememberCoroutineScope()
    var temp by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    coroutineScope.launch(Dispatchers.IO) {
        //using retrofit
        val weatherServices = WeatherClient.getWeatherServices()

        val response = weatherServices.getWeather(
            lat = latitudeTwoDecimal,
            lon = longitudeTwoDecimal,
            apiKey = api_id1
        )
        temp = response.main.temp.minus(273.00).times(100.00).roundToInt().div(100.0)
            .toString()
        name = response.name
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = temp)
        Text(text = name.unaccent())
        Spacer(modifier = Modifier.size(100.dp))
        Text(text = "Latitude: ${latitude.value}")
        Text(text = "Longitude: ${longitude.value}")
        Spacer(modifier = Modifier.size(100.dp))
        Text(text = "Latitude(DMS) $latitudeDegree\u00B0${latitudeMinute.toInt()}'${latitudeSec.toInt()}\"")
        Text(text = "Longitude(DMS) $longitudeDegree\u00B0${longitudeMinute.toInt()}'${longitudeSec.toInt()}\"")

        if (weatherViewModel.isOnline()) {
            Text(text = "Device Internet : ON")
        } else {
            Text(text = "Device Internet : OFF")
        }
    }

}





