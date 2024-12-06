package com.example.projekmap

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
        Glide.with(holder.itemView.context).load(place.vendorImage).into(holder.placeImage)
        holder.placeName.text = place.vendor
        holder.placeLocation.text = place.place
        holder.placeRating.text = place.avgRating.toString()

        // Handle item click
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlaceDetailActivity::class.java).apply {
                putExtra("PLACE_ID", place.id)
                putExtra("PLACE_NAME", place.vendor)
                putExtra("PLACE_LOCATION", place.place)
                putExtra("PLACE_DESCRIPTION", place.desc)
                putExtra("PLACE_IMAGE", place.vendorImage)
                putExtra("PLACE_RATING", place.avgRating.toString())
                putExtra("PLACE_TYPE", place.type)
                putExtra("PLACE_ESTIMATE", place.estimate)
                putExtra("PLACE_VIA", place.via)
            }
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return places.size
    }
}
