package com.example.locationbasisnew

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(private val locationList: List<Location>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textLatitude: TextView = itemView.findViewById(R.id.lat)
        val textLongitude: TextView = itemView.findViewById(R.id.Long)
        val textTimestamp: TextView = itemView.findViewById(R.id.TimeStamp)
        val textAddress: TextView = itemView.findViewById(R.id.Address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_location, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val locationData = locationList[position]
        holder.textLatitude.text = "Latitude: ${locationData.Latitude}"
        holder.textLongitude.text = "Longitude: ${locationData.Longitude}"
        holder.textTimestamp.text = "Timestamp: ${locationData.TimeStamp}"
        holder.textAddress.text = "Address: ${locationData.Address}"
    }

    override fun getItemCount(): Int {
        return locationList.size
    }
}
