package com.example.projekmap

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class NearestLocationAdapter(private val items: List<Place>) : RecyclerView.Adapter<NearestLocationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.nearest_location_image)
        val name: TextView = itemView.findViewById(R.id.nearest_location_name)
        val location: TextView = itemView.findViewById(R.id.nearest_location_address)
        val rating: TextView = itemView.findViewById(R.id.nearest_location_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_nearest_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = items[position]
        holder.image.setImageResource(place.imageRes)
        holder.name.text = place.name
        holder.location.text = place.location
        holder.rating.text = place.rating.toString()

        // Aksi klik untuk membuka detail tempat
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PlaceDetailActivity::class.java).apply {
                putExtra("PLACE_NAME", place.name)
                putExtra("PLACE_LOCATION", place.location)
                putExtra("PLACE_RATING", place.rating.toString())
                putExtra("PLACE_IMAGE", place.imageRes)
                putExtra("PLACE_DESCRIPTION", place.description)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
