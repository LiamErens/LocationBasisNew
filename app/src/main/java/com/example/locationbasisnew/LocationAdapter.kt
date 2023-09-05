package com.example.locationbasisnew
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LocationAdapter(private val locationList: List<LocationData>) :
    RecyclerView.Adapter<LocationAdapter.LocationViewHolder>() {

    class LocationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textLatitude: TextView = itemView.findViewById(R.id.lat)
        val textLongitude: TextView = itemView.findViewById(R.id.Long)
        val textAddress: TextView = itemView.findViewById(R.id.Address)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LocationViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_location, parent, false)
        return LocationViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: LocationViewHolder, position: Int) {
        val locationData = locationList[position]
        holder.textLatitude.text = "Latitude: ${locationData.latitude}"
        holder.textLongitude.text = "Longitude: ${locationData.longitude}"
        holder.textAddress.text = "Address: ${locationData.address}"
    }

    override fun getItemCount(): Int {
        return locationList.size
    }
}
