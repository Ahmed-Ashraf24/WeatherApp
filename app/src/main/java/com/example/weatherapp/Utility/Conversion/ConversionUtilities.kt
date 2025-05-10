package com.example.weatherapp.Utility.Conversion

object ConversionUtilities {



        fun toCelsius(degreeFahrenheit: Double): Int {
            return ((degreeFahrenheit - 32) * (5.0 / 9.0)).toInt()
        }


        fun toFahrenheit(degreeCelsius: Double): Int {
            return ((degreeCelsius * 9.0 / 5.0) + 32).toInt()
        }

}