package com.example.projekmap

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PopularNearbyAdapter(private val items: List<Place>) : RecyclerView.Adapter<PopularNearbyAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.image)
        val placeName: TextView = itemView.findViewById(R.id.place_name)
        val placeLocation: TextView = itemView.findViewById(R.id.place_location)
        val placeRating: TextView = itemView.findViewById(R.id.place_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_popular_nearby, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = items[position]
        holder.image.setImageResource(place.imageRes)
        holder.placeName.text = place.name
        holder.placeLocation.text = place.location
        holder.placeRating.text = place.rating.toString()

        // Set an onClickListener for each item to navigate to the PlaceDetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlaceDetailActivity::class.java)
            intent.putExtra("PLACE_NAME", place.name)
            intent.putExtra("PLACE_LOCATION", place.location)
            intent.putExtra("PLACE_RATING", place.rating.toString())
            intent.putExtra("PLACE_IMAGE", place.imageRes)
            intent.putExtra("PLACE_DESCRIPTION", place.description) // Mengirim deskripsi
            holder.itemView.context.startActivity(intent)
        }

        // Debug log
        Log.d("PopularNearbyAdapter", "Binding item: ${place.name} at position $position")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
