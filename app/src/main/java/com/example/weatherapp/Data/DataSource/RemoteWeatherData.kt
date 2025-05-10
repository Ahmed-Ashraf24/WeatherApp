package com.example.weatherapp.Data.DataSource

import android.annotation.SuppressLint
import android.util.Log
import com.example.weatherapp.Data.Mapper.DailyWeatherMapper
import com.example.weatherapp.Data.Model.DailyWeatherInfo
import com.example.weatherapp.Domain.Entity.DailyWeather
import com.example.weatherapp.Utility.Conversion.ConversionUtilities
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.collections.ArrayList

class RemoteWeatherData {
    private val key="ULASGB3WV9UQXP3GGP2MTHULJ"
    @SuppressLint("SuspiciousIndentation")
    fun getWeatherInfo(lat:Double, long:Double):List<DailyWeather>{

        var connection: HttpURLConnection? = null
        return try{
    val url= URL("https://weather.visualcrossing.com/VisualCrossingWebServices/rest/services/timeline/$lat,$long/?key=$key&include=current")
        connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        val responseCode = connection.responseCode
            if (responseCode == HttpURLConnection.HTTP_OK) {
            val reader = BufferedReader(InputStreamReader(connection.inputStream))
            val response = StringBuilder()
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                response.append(line)
            }

            reader.close()

                val weatherInfoList=parseWeatherJson(jsonString = response.toString())
                val weatherList=mutableListOf<DailyWeather>()
                weatherInfoList.forEach {
                    weatherList.add(DailyWeatherMapper.toDailyWeather(it,
                        when(weatherInfoList.indexOf(it)){
                            0->"${ConversionUtilities.toCelsius(it.currentTemp)}°C"
                            else->"${ConversionUtilities.toCelsius(it.minTemp)}°C/${ConversionUtilities.toCelsius(it.maxTemp)}°C"
                        }
                        ))
                }

                return weatherList
        } else {
            Log.d("Server returned HTTP :", responseCode.toString())
            return emptyList()
        }}catch (e:Exception){
            e.printStackTrace()

            emptyList()
        }
        finally {
            connection?.disconnect()
        }

    }
    fun parseWeatherJson(jsonString: String): List<DailyWeatherInfo> {
        val result = mutableListOf<DailyWeatherInfo>()

        val root = JSONObject(jsonString)

        val locationName = root.getString("timezone")
        val forecastArray = root.getJSONArray("days")
        val currentConditionObject=root.getJSONObject("currentConditions")
        for (i in 0 until forecastArray.length()) {
            val dayObject = forecastArray.getJSONObject(i)
            val dateStr = dayObject.getString("datetime")

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.parse(dateStr)
            val dayName = SimpleDateFormat("EEEE", Locale.getDefault()).format(date ?: Date())
            val minTemp = dayObject.getDouble("tempmin")
            val temp=when(i) {
             0->   currentConditionObject.getDouble("temp")
                else->dayObject.getDouble("temp")
            }
            val maxTemp = dayObject.getDouble("tempmax")

            val description = dayObject.getString("description")
            val icon = when(i){
                0->   currentConditionObject.getString("icon")
                else->dayObject.getString("icon")
            }
            val feelsLike=when(i){
                0->currentConditionObject.getDouble("feelslike")
                else->dayObject.getDouble("feelslike")
            }
            val humidity=when(i){
                0->currentConditionObject.getDouble("humidity")
                else->dayObject.getDouble("humidity")
            }
            val uv=when(i){
                0->currentConditionObject.getDouble("uvindex")
                else->dayObject.getDouble("uvindex")
            }
            val visibility=when(i){
                0->currentConditionObject.getDouble("visibility")
                else->dayObject.getDouble("visibility")
            }
            val pressure=when(i){
                0->currentConditionObject.getDouble("pressure")
                else->dayObject.getDouble("pressure")
            }
            val windSpeed=when(i){
                0->currentConditionObject.getDouble("windspeed")
                else->dayObject.getDouble("windspeed")
            }
            result.add(
                DailyWeatherInfo(
                    date = dateStr,
                    dayName = dayName,
                    currentTemp = temp,
                    maxTemp = maxTemp,
                    minTemp = minTemp,
                    description = description,
                    icon = icon,
                    locationName = locationName
                    , windSpeed = windSpeed,
                    pressure = pressure,
                    visibility = visibility,
                    uv = uv,
                    humidity = humidity,
                    feelsLike = feelsLike
                )
            )
        }
        return result
    }

}