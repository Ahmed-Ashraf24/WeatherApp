package com.example.weatherapp.presentation.Screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.weatherapp.R
import com.example.weatherapp.databinding.FragmentTodayBinding
import com.example.weatherapp.presentation.ViewModel.DailyWeatherViewModel


private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class TodayFragment : Fragment() {
    lateinit var binding: FragmentTodayBinding

    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding=FragmentTodayBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityInstance=(activity as? MainActivity)
        activityInstance!!.dailyWeatherViewModel.todayWeather.observe(viewLifecycleOwner) {
                result ->
            result.fold(
                onSuccess = {dailyWeather->

                    activityInstance!!.dailyWeather=dailyWeather
                    binding.tempToday.text=dailyWeather!!.temperature
                    binding.iconToday.setImageResource(dailyWeather!!.iconRes)
                    binding.cityName.text=dailyWeather.locationName
                    binding.headerToday.text=dailyWeather.dayName
                    binding.conditionToday.text=dailyWeather.description
                    binding.humidity.text=dailyWeather.humidity
                    binding.feelsLike.text=dailyWeather.feelsLike
                    binding.wind.text=dailyWeather.windSpeed
                    binding.pressure.text=dailyWeather.pressure
                    binding.uvIndex.text=dailyWeather.uv
                    binding.visibility.text=dailyWeather.visibility

                }
                , onFailure = {exception ->
                    exception.printStackTrace()
                    Toast.makeText(requireContext(),exception.message.toString(), Toast.LENGTH_LONG).show()
                    Log.d("data exception ",exception.toString())


                })



    }
        binding.swipeRefresh.setOnRefreshListener {
            if(activityInstance.isNetworkAvailable()){
            activityInstance.checkLocationPermissionAndFetchData()
            binding.swipeRefresh.isRefreshing = false}
            else{
                activityInstance.dailyWeatherViewModel.getCachedWeather()

                Toast.makeText(requireContext(),"Check your network connectivity and then refresh",Toast.LENGTH_LONG).show()
                binding.swipeRefresh.isRefreshing = false

            }
        }
    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TodayFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}