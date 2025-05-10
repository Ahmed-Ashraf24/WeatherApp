package com.example.weatherapp.presentation.Screens

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.weatherapp.Domain.Entity.ForecastWeather
import com.example.weatherapp.Utility.UIAdapters.ForecastAdapter
import com.example.weatherapp.databinding.FragmentForecastBinding

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class ForecastFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    lateinit var binding:FragmentForecastBinding
    var forecastItems = listOf<ForecastWeather>()
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
        binding=FragmentForecastBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val activityInstance=(activity as? MainActivity)
        activityInstance!!.dailyWeatherViewModel.forecastList.observe(viewLifecycleOwner) {
                result ->
            Log.d("if result os sucssess",result.isSuccess.toString())
            result.fold(
                onSuccess = {forecast->
                    forecastItems=forecast
                    binding.recyclerViewForecast.layoutManager=LinearLayoutManager(requireContext())
                    binding.recyclerViewForecast.adapter=ForecastAdapter(forecastItems)

                }
                , onFailure = {exception ->
                    Toast.makeText(requireContext(),exception.message, Toast.LENGTH_LONG).show()
                    Log.d("data exception ",exception.message.toString())


                })


        }


    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ForecastFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}