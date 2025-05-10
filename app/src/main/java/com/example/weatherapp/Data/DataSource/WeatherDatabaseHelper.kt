package com.example.weatherapp.Data.DataSource

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import com.example.weatherapp.Domain.Entity.DailyWeather

class WeatherDatabaseHelper private constructor(context: Context) :
    SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    companion object {
        private const val DATABASE_NAME = "weather.db"
        private const val DATABASE_VERSION = 2

        private const val TABLE_WEATHER = "weather"
        private const val COLUMN_ID = "id"
        private const val COLUMN_DAY_NAME = "day_name"
        private const val COLUMN_TEMPERATURE = "temperature"
        private const val COLUMN_DESCRIPTION = "description"
        private const val COLUMN_FEELS_LIKE = "feels_like"
        private const val COLUMN_HUMIDITY = "humidity"
        private const val COLUMN_UV = "uv"
        private const val COLUMN_VISIBILITY = "visibility"
        private const val COLUMN_PRESSURE = "pressure"
        private const val COLUMN_WIND_SPEED = "wind_speed"
        private const val COLUMN_ICON = "icon"
        private const val COLUMN_LOCATION_NAME = "location_name"

        @Volatile
        private var INSTANCE: WeatherDatabaseHelper? = null

        fun getInstance(context: Context): WeatherDatabaseHelper {
            return INSTANCE ?: synchronized(this) {
                val instance = WeatherDatabaseHelper(context.applicationContext)
                INSTANCE = instance
                instance
            }
        }
    }

    override fun onCreate(db: SQLiteDatabase) {
        val createTable = """
            CREATE TABLE $TABLE_WEATHER (
         $COLUMN_ID INTEGER PRIMARY KEY,
                $COLUMN_DAY_NAME TEXT,
                $COLUMN_TEMPERATURE TEXT,
                $COLUMN_DESCRIPTION TEXT,
                $COLUMN_FEELS_LIKE TEXT,
                $COLUMN_HUMIDITY TEXT,
                $COLUMN_UV TEXT,
                $COLUMN_VISIBILITY TEXT,
                $COLUMN_PRESSURE TEXT,
                $COLUMN_WIND_SPEED TEXT,
                $COLUMN_ICON NUMBER,
                $COLUMN_LOCATION_NAME TEXT
            )
        """.trimIndent()
        db.execSQL(createTable)
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        db.execSQL("DROP TABLE IF EXISTS $TABLE_WEATHER")
        onCreate(db)
    }

    fun upsertWeather(helper: WeatherDatabaseHelper, weatherList: List<DailyWeather>) {
        val db = helper.writableDatabase

        db.beginTransaction()
        try {
            val cursor = db.rawQuery("SELECT COUNT(*) FROM weather", null)
            cursor.moveToFirst()
            val count = cursor.getInt(0)
            cursor.close()

            if (count > 0) {

                db.delete("weather", null, null)
            }
            for (weather in weatherList) {
                val values = ContentValues().apply {
                    put(COLUMN_DAY_NAME, weather.dayName)
                    put(COLUMN_TEMPERATURE, weather.temperature)
                    put(COLUMN_DESCRIPTION, weather.description)
                    put(COLUMN_FEELS_LIKE, weather.feelsLike)
                    put(COLUMN_HUMIDITY, weather.humidity)
                    put(COLUMN_UV, weather.uv)
                    put(COLUMN_VISIBILITY, weather.visibility)
                    put(COLUMN_PRESSURE, weather.pressure)
                    put(COLUMN_WIND_SPEED, weather.windSpeed)
                    put(COLUMN_ICON, weather.iconRes)
                    put(COLUMN_LOCATION_NAME, weather.locationName)
                }

                db.insert("weather", null, values)
            }

            db.setTransactionSuccessful()
        } finally {
            db.endTransaction()
            db.close()
        }
    }

    fun getCachedWeatherList(helper: WeatherDatabaseHelper): List<DailyWeather> {
        val db = helper.readableDatabase
        val cursor = db.query(
            "weather",
            null,
            null,
            null,
            null,
            null,
           null
        )

        val weatherList = mutableListOf<DailyWeather>()

        while (cursor.moveToNext()) {
            val weather = DailyWeather(
                dayName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DAY_NAME)),
                temperature = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TEMPERATURE)),
                description = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESCRIPTION)),
                feelsLike = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_FEELS_LIKE)),
                humidity = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_HUMIDITY)),
                uv = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_UV)),
                visibility = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_VISIBILITY)),
                pressure = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_PRESSURE)),
                windSpeed = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_WIND_SPEED)),
                iconRes = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ICON)),
                locationName = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_LOCATION_NAME))
            )
            weatherList.add(weather)
        }

        cursor.close()
        db.close()
        return weatherList
    }
}
