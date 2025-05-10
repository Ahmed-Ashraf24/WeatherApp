package com.example.weatherapp.Utility.UIAdapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.weatherapp.Domain.Entity.ForecastWeather
import com.example.weatherapp.R

class ForecastAdapter(private val items: List<ForecastWeather>) :
    RecyclerView.Adapter<ForecastAdapter.ForecastViewHolder>() {

    class ForecastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val dayName: TextView = itemView.findViewById(R.id.day_name)
        val iconWeather: ImageView = itemView.findViewById(R.id.icon_weather)
        val dayTemp: TextView = itemView.findViewById(R.id.day_temp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ForecastViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_forecast, parent, false)
        return ForecastViewHolder(view)
    }

    override fun onBindViewHolder(holder: ForecastViewHolder, position: Int) {
        val item = items[position]
        holder.dayName.text = item.dayName
        holder.iconWeather.setImageResource(item.iconResId)
        holder.dayTemp.text = item.temperatureRange
    }

    override fun getItemCount() = items.size
}