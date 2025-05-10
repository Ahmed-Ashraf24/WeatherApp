package com.example.weatherapp.Domain.RepoInterface

import com.example.weatherapp.Domain.Entity.DailyWeather

interface DailyWeatherRepository {
    fun getDailyWeather(lat:Double,long:Double):List<DailyWeather>
    fun getCachedWeather():List<DailyWeather>
}