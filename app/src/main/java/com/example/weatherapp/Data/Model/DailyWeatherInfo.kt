package com.example.weatherapp.Data.Model

data class DailyWeatherInfo( val date: String,
                             val dayName: String,
                             val minTemp:Double,
                             val maxTemp:Double,
                             val currentTemp :Double,
                             val feelsLike:Double,
                             val humidity:Double,
                             val uv:Double,
                             val visibility:Double,
                             val pressure:Double,
                             val windSpeed:Double,
                             val description : String,
                             val icon : String,
                             val locationName: String
)
