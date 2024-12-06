package com.example.projekmap

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

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
        Glide.with(holder.itemView.context).load(place.vendorImage).into(holder.image)
        holder.placeName.text = place.vendor
        holder.placeLocation.text = place.place
        holder.placeRating.text = place.avgRating.toString()

        // Set an onClickListener for each item to navigate to the PlaceDetailActivity
        holder.itemView.setOnClickListener {
            val intent = Intent(holder.itemView.context, PlaceDetailActivity::class.java)
            intent.putExtra("PLACE_ID", place.id)
            intent.putExtra("PLACE_NAME", place.vendor)
            intent.putExtra("PLACE_LOCATION", place.place)
            intent.putExtra("PLACE_RATING", place.avgRating.toString())
            intent.putExtra("PLACE_IMAGE", place.vendorImage)
            intent.putExtra("PLACE_DESCRIPTION", place.desc) // Mengirim deskripsi
            intent.putExtra("PLACE_TYPE", place.type)
            intent.putExtra("PLACE_ESTIMATE", place.estimate)
            intent.putExtra("PLACE_VIA", place.via)
            holder.itemView.context.startActivity(intent)
        }

        // Debug log
        Log.d("PopularNearbyAdapter", "Binding item: ${place.vendor} at position $position")
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
