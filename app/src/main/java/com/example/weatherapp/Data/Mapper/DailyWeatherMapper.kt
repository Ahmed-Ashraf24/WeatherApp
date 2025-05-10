package com.example.weatherapp.Data.Mapper

import com.example.weatherapp.Data.Model.DailyWeatherInfo
import com.example.weatherapp.Domain.Entity.DailyWeather
import com.example.weatherapp.Domain.Entity.ForecastWeather
import com.example.weatherapp.Utility.Constraints.IconConstraints
import com.example.weatherapp.Utility.Conversion.ConversionUtilities

class DailyWeatherMapper {
    companion object{
    fun toDailyWeather(dailyWeatherInfo: DailyWeatherInfo,temperature:String): DailyWeather {
        return DailyWeather(
            dayName = dailyWeatherInfo.dayName,
            temperature = temperature,
            description = dailyWeatherInfo.description,
            iconRes = IconConstraints.getWeatherIcon(dailyWeatherInfo.icon),
            locationName = dailyWeatherInfo.locationName,
            feelsLike = "${ConversionUtilities.toCelsius(dailyWeatherInfo.feelsLike)}°C",
            humidity = "${dailyWeatherInfo.humidity}%",
            windSpeed = "${dailyWeatherInfo.windSpeed} km/h",
            visibility = "${dailyWeatherInfo.visibility}km",
            pressure = "${dailyWeatherInfo.pressure} hPa",
            uv = when(dailyWeatherInfo.uv){
                in 0.0..2.0 -> "Low"
                in 3.0..5.0 -> "Moderate"
                in 6.0..7.0 -> "High"
                in 8.0..10.0 -> "Very High"
                else -> "Extreme"
            }
        )

    }
    fun toForecastItem(dailyWeatherData: DailyWeather): ForecastWeather {
        return ForecastWeather(
            dayName = dailyWeatherData.dayName,
            temperatureRange = dailyWeatherData.temperature,
            iconResId = dailyWeatherData.iconRes
        )
    }

    }
}