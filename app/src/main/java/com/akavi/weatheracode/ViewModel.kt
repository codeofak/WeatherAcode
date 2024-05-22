package com.akavi.weatheracode

import android.app.Application
import androidx.lifecycle.AndroidViewModel

class ViewModel(application:Application): AndroidViewModel(application) {

    val context = getApplication<Application>()


}