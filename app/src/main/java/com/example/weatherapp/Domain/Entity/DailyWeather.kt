package com.example.weatherapp.Domain.Entity

data class DailyWeather(val dayName: String,
                        val temperature: String,
                        val description: String,
                        val feelsLike:String,
                        val humidity:String,
                        val uv:String,
                        val visibility:String,
                        val pressure:String,
                        val windSpeed:String,
                        val iconRes: Int,
                        val locationName: String
)
