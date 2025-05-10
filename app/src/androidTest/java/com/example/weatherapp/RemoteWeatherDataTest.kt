package com.example.weatherapp

import com.example.weatherapp.Data.DataSource.RemoteWeatherData
import com.example.weatherapp.Utility.Conversion.ConversionUtilities
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class RemoteWeatherDataTest {

    private lateinit var remoteWeatherData: RemoteWeatherData

    @Before
    fun setUp() {
        remoteWeatherData = RemoteWeatherData()
    }

    @Test
    fun parseWeatherJsonReturnsCorrectListForMultipleDays() {
        val json = """
            {
                "timezone": "America/New_York",
                "currentConditions": {
                    "temp": 70,
                    "feelslike": 68,
                    "humidity": 50,
                    "uvindex": 5,
                    "visibility": 10,
                    "pressure": 1010,
                    "windspeed": 10,
                    "icon": "partly-cloudy-day"
                },
                "days": [
                    {
                        "datetime": "2025-05-10",
                        "tempmin": 60,
                        "temp": 70,
                        "tempmax": 75,
                        "description": "Partly cloudy",
                        "icon": "partly-cloudy-day",
                        "feelslike": 68,
                        "humidity": 50,
                        "uvindex": 5,
                        "visibility": 10,
                        "pressure": 1010,
                        "windspeed": 10
                    },
                    {
                        "datetime": "2025-05-11",
                        "tempmin": 58,
                        "temp": 68,
                        "tempmax": 73,
                        "description": "Sunny",
                        "icon": "clear-day",
                        "feelslike": 67,
                        "humidity": 45,
                        "uvindex": 6,
                        "visibility": 10,
                        "pressure": 1012,
                        "windspeed": 12
                    }
                ]
            }
        """.trimIndent()

        val result = remoteWeatherData.parseWeatherJson(json)

        assertEquals(2, result.size)

        val firstDay = result[0]
        assertEquals("2025-05-10", firstDay.date)
        assertEquals("America/New_York", firstDay.locationName)
        assertEquals("Partly cloudy", firstDay.description)
        assertEquals(70.0, firstDay.currentTemp, 0.01)

        val secondDay = result[1]
        assertEquals("2025-05-11", secondDay.date)
        assertEquals("Sunny", secondDay.description)
        assertEquals(68.0, secondDay.currentTemp, 0.01)
    }

    @Test
    fun toCelsiusCorrectlyConvertsFahrenheitToCelsius() {
        val fahrenheit = 68.0
        val expectedCelsius = 20
        val actualCelsius = ConversionUtilities.toCelsius(fahrenheit)
        assertEquals(expectedCelsius, actualCelsius)
    }
}
