package com.example.projekmap

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class PlaceAdapter(private val places: List<Place>) : RecyclerView.Adapter<PlaceAdapter.PlaceViewHolder>() {

    class PlaceViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val placeImage: ImageView = itemView.findViewById(R.id.place_image)
        val placeName: TextView = itemView.findViewById(R.id.place_name)
        val placeLocation: TextView = itemView.findViewById(R.id.place_location)
        val placeRating: TextView = itemView.findViewById(R.id.place_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_place, parent, false)
        return PlaceViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlaceViewHolder, position: Int) {
        val place = places[position]
        holder.placeImage.setImageResource(place.imageRes)
        holder.placeName.text = place.name
        holder.placeLocation.text = place.location
        holder.placeRating.text = place.rating.toString()

        // Handle item click
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlaceDetailActivity::class.java).apply {
                putExtra("PLACE_NAME", place.name)
                putExtra("PLACE_LOCATION", place.location)
                putExtra("PLACE_DESCRIPTION", place.description)
                putExtra("PLACE_IMAGE", place.imageRes)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }
}
