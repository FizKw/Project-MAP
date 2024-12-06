package com.example.projekmap

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class RecommendedAdapter(private val items: List<Place>) : RecyclerView.Adapter<RecommendedAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.recommended_image)
        val placeName: TextView = itemView.findViewById(R.id.recommended_place_name)
        val placeLocation: TextView = itemView.findViewById(R.id.recommended_place_location)
        val placeRating: TextView = itemView.findViewById(R.id.recommended_place_rating)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate layout baru yang telah disesuaikan
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_recommended, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val place = items[position]

        // Set data ke views yang ada di layout
        Glide.with(holder.itemView.context).load(place.vendorImage).into(holder.image)
//        holder.image.setImageResource(R.drawable.raja_ampat)
        holder.placeName.text = place.vendor
        holder.placeLocation.text = place.place
        holder.placeRating.text = place.avgRating.toString()

        // Aksi klik untuk membuka detail tempat
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, PlaceDetailActivity::class.java).apply {
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
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
