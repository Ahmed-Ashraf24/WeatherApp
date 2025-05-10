package com.example.weatherapp.Utility.Constraints

import com.example.weatherapp.R

object IconConstraints {
    val weatherIconMap = mapOf(

        "clear-day" to R.drawable.sun,
        "clear-night" to R.drawable.moon,


        "cloudy" to R.drawable.cloudy,
        "partly-cloudy-day" to R.drawable.sun_clouds,
        "partly-cloudy-night" to R.drawable.moon_clouds,


        "rain" to R.drawable.rain,
        "showers-day" to R.drawable.sun_rain,
        "showers-night" to R.drawable.moon_and_rain,


        "snow" to R.drawable.snow,
        "snow-showers-day" to R.drawable.snow,
        "snow-showers-night" to R.drawable.thunderstorm_snow,

        "thunder-rain" to R.drawable.thunder,
        "thunder-showers-day" to R.drawable.thunder,
        "thunder-showers-night" to R.drawable.thunder,

        "fog" to R.drawable.fog,
        "wind" to R.drawable.windy,


        "cold" to R.drawable.snow,

        
    )

    fun getWeatherIcon(condition: String, isDaytime: Boolean = true): Int {
        return weatherIconMap[condition]
            ?: if (isDaytime) R.drawable.sun_clouds else R.drawable.moon_clouds
    }
}