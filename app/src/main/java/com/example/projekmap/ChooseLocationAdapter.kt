package com.example.projekmap

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ChooseLocationAdapter(private val items: List<Place>) : RecyclerView.Adapter<ChooseLocationAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val image: ImageView = itemView.findViewById(R.id.location_image)
        val name: TextView = itemView.findViewById(R.id.location_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_choose_location, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val location = items[position]
        holder.image.setImageResource(location.imageRes)
        holder.name.text = location.name

        // Aksi klik untuk membuka detail tempat
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, LocationDetailActivity::class.java).apply {
                putExtra("LOCATION_NAME", location.name)
                putExtra("LOCATION_IMAGE", location.imageRes)
                putExtra("LOCATION_DESCRIPTION", location.description)
            }
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
