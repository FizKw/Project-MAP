package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TripAdapter(private val tripList: List<Trip>) : RecyclerView.Adapter<TripAdapter.TripViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TripViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_trip, parent, false)
        return TripViewHolder(view)
    }

    override fun onBindViewHolder(holder: TripViewHolder, position: Int) {
        val trip = tripList[position]
        holder.tripName.text = trip.name
        holder.tripLocation.text = trip.location
        holder.tripDate.text = "Date: ${trip.date}"
        holder.tripPrice.text = "Price: ${trip.price}"
        holder.tripImage.setImageResource(trip.imageRes)
        holder.tripStatus.text = "Successfully Booked"
    }

    override fun getItemCount(): Int {
        return tripList.size
    }

    class TripViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tripName: TextView = itemView.findViewById(R.id.trip_name)
        val tripLocation: TextView = itemView.findViewById(R.id.trip_location)
        val tripDate: TextView = itemView.findViewById(R.id.trip_date)
        val tripPrice: TextView = itemView.findViewById(R.id.trip_price)
        val tripImage: ImageView = itemView.findViewById(R.id.trip_image)
        val tripStatus: TextView = itemView.findViewById(R.id.trip_status)
    }
}
