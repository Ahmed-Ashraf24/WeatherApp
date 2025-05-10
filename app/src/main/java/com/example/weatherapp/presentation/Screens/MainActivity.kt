package com.example.weatherapp.presentation.Screens

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.IntentSender
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.example.weatherapp.Domain.Entity.DailyWeather
import com.example.weatherapp.R
import com.example.weatherapp.databinding.ActivityMainBinding
import com.example.weatherapp.presentation.ViewModel.DailyWeatherViewModel
import com.google.android.gms.common.api.ResolvableApiException
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.LocationSettingsRequest
import com.google.android.gms.location.Priority

class MainActivity : AppCompatActivity() {
    private val LOCATION_PERMISSION_REQUEST_CODE = 1
    lateinit var binding:ActivityMainBinding
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    val dailyWeatherViewModel:DailyWeatherViewModel by viewModels()
    var dailyWeather: DailyWeather? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding=ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if(isNetworkAvailable()) {
            checkLocationPermissionAndFetchData()
        }
        else{
            dailyWeatherViewModel.getCachedWeather()
            Toast.makeText(this,"Check your network connectivity and then refresh",Toast.LENGTH_LONG).show()
        }

        if(savedInstanceState==null){
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, TodayFragment())
                .commit()
        }
        binding.bottomNavigation.setOnItemSelectedListener {item->
            var selectedFragment: Fragment = TodayFragment()
            when (item.itemId) {
                R.id.nav_today -> selectedFragment = TodayFragment()
                R.id.nav_forecast -> selectedFragment = ForecastFragment()
            }

            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, selectedFragment)
                .commit()

            true

        }
    }
    fun isNetworkAvailable(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork ?: return false
        val capabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
        return when {
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    }
     fun checkLocationPermissionAndFetchData() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {

            getLocationAndData()
        } else {

            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        }
    }

    @SuppressLint("MissingPermission")
     private fun getLocationAndData() {
        val locationRequest = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 1000L)
            .setWaitForAccurateLocation(false)
            .setMinUpdateIntervalMillis(1000L)
            .setMaxUpdateDelayMillis(5000L)
            .build()

        val builder = LocationSettingsRequest.Builder()
            .addLocationRequest(locationRequest)

        val settingsClient = LocationServices.getSettingsClient(this)
        val task = settingsClient.checkLocationSettings(builder.build())

        task.addOnSuccessListener {
            fusedLocationClient.getCurrentLocation(
                Priority.PRIORITY_HIGH_ACCURACY,
                null)
                .addOnSuccessListener { location ->
                    if (location != null) {
                        dailyWeatherViewModel.getRemoteWeather( location.latitude,location.longitude)
                        Toast.makeText(this, "Data returned", Toast.LENGTH_SHORT).show()

                    } else {
                        Toast.makeText(this, "Location is null, Check if the GPS is opened and refresh", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to get location: ${e.message}", Toast.LENGTH_SHORT).show()
                }        }

        task.addOnFailureListener { exception ->
            if (exception is ResolvableApiException) {
                try {
                    exception.startResolutionForResult(this, 2)
                } catch (sendEx: IntentSender.SendIntentException) {
                    Toast.makeText(this,sendEx.message,Toast.LENGTH_SHORT).show()
                }
            }
        }

    }

    override fun onActivityResult(
        requestCode: Int, resultCode: Int, data: Intent?
    ) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 2) {
            when (resultCode) {
                Activity.RESULT_OK -> {
                    getLocationAndData()
                }

                Activity.RESULT_CANCELED -> {
                    Toast.makeText(this, "You Need To Turn On GPS to get the Weather of your current location \n Open GPS and refresh the page ", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLocationAndData()
            } else {

                Toast.makeText(this, "Location permission denied.", Toast.LENGTH_SHORT).show()
            }
        }

}

}