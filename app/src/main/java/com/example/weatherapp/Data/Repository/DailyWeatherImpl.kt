package com.example.weatherapp.Data.Repository

import com.example.weatherapp.Data.DataSource.RemoteWeatherData
import com.example.weatherapp.Data.DataSource.WeatherDatabaseHelper
import com.example.weatherapp.Domain.Entity.DailyWeather
import com.example.weatherapp.Domain.RepoInterface.DailyWeatherRepository
import com.example.weatherapp.MyApplication

class DailyWeatherImpl:DailyWeatherRepository {
    private val remoteWeatherData=RemoteWeatherData()
    private val localWeatherData=WeatherDatabaseHelper.getInstance(MyApplication().getContext())
    override fun getDailyWeather(lat:Double,long:Double): List<DailyWeather> {

        val dailyWeatherList= remoteWeatherData.getWeatherInfo(lat,long)
        cachingWeather(dailyWeatherList)
        return dailyWeatherList
    }

    private fun cachingWeather(dailyWeatherList: List<DailyWeather>) {
        with(localWeatherData) {
            upsertWeather(this,dailyWeatherList)
        }
    }

    override fun getCachedWeather(): List<DailyWeather> {
        with(localWeatherData){
            return getCachedWeatherList(this)
        }

    }
}