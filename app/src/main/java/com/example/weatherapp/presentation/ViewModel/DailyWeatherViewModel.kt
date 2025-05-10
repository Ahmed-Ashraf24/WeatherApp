package com.example.weatherapp.presentation.ViewModel

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.weatherapp.Domain.Entity.DailyWeather
import com.example.weatherapp.Domain.Entity.ForecastWeather
import com.example.weatherapp.Domain.UseCase.LocalWeatherUseCase
import com.example.weatherapp.Domain.UseCase.RemoteWeatherUseCase

class DailyWeatherViewModel:ViewModel() {
    private val _todayWeather = MutableLiveData<Result<DailyWeather>>()
    val todayWeather: LiveData<Result<DailyWeather>> = _todayWeather
    private val _forecastList = MutableLiveData<Result<List<ForecastWeather>>>()
    val forecastList: LiveData<Result<List<ForecastWeather>>> get() = _forecastList
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    fun getRemoteWeather(lat:Double, long:Double){
        _isLoading.value = true

        Thread {
            try {
                val dailyWeatherData = RemoteWeatherUseCase(lat, long)
                val todayWeather = dailyWeatherData.getTodayWeather()
                val forecast = dailyWeatherData.getForecastForFiveDays()

                Handler(Looper.getMainLooper()).post {
                    _todayWeather.value = Result.success(todayWeather)
                    _forecastList.value = Result.success(forecast)
                    _isLoading.value = false
                }

            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    _todayWeather.value = Result.failure(e)
                    _forecastList.value = Result.failure(e)
                    _isLoading.value = false
                }
            }
        }.start()
    }
    fun getCachedWeather(){
        Thread {
            try {
                val dailyWeatherData = LocalWeatherUseCase()
                val todayWeather = dailyWeatherData.getTodayWeather()
                val forecast = dailyWeatherData.getForecastForFiveDays()

                Handler(Looper.getMainLooper()).post {
                    _todayWeather.value = Result.success(todayWeather)
                    _forecastList.value = Result.success(forecast)
                    _isLoading.value = false
                }
            } catch (e: Exception) {
                Handler(Looper.getMainLooper()).post {
                    _todayWeather.value = Result.failure(e)
                    _forecastList.value = Result.failure(e)
                    _isLoading.value = false
                }
            }
        }.start()
    }


}