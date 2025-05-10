package com.example.weatherapp.Domain.UseCase

import android.util.Log
import com.example.weatherapp.Data.Mapper.DailyWeatherMapper
import com.example.weatherapp.Data.Repository.DailyWeatherImpl
import com.example.weatherapp.Domain.Entity.DailyWeather
import com.example.weatherapp.Domain.Entity.ForecastWeather
import com.example.weatherapp.Domain.RepoInterface.DailyWeatherRepository

class RemoteWeatherUseCase(val lat: Double, val long: Double) {

    private val remoteDailyWeather: DailyWeatherRepository = DailyWeatherImpl()
    private val dailyWeatherList = mutableListOf<DailyWeather>()

    init {
        dailyWeatherList.addAll(remoteDailyWeather.getDailyWeather(lat, long))
        Log.d("daily data",dailyWeatherList[0].toString())

    }

    fun getTodayWeather(): DailyWeather {
        return dailyWeatherList[0]
    }

    fun getForecastForFiveDays(range : Int = 5): List<ForecastWeather> {
        val forecastList = ArrayList<ForecastWeather>()
        dailyWeatherList.slice(1..range).forEach {

            forecastList.add(DailyWeatherMapper.toForecastItem(it))

        }
        return forecastList
    }



}