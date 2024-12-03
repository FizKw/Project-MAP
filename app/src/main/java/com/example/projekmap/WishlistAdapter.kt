package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class WishlistAdapter(private val items: List<WishlistItem>) :
    RecyclerView.Adapter<WishlistAdapter.WishlistViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WishlistViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_wishlist, parent, false)
        return WishlistViewHolder(view)
    }

    override fun onBindViewHolder(holder: WishlistViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = items.size

    inner class WishlistViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage: ImageView = itemView.findViewById(R.id.item_image)
        private val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        private val itemLocation: TextView = itemView.findViewById(R.id.item_location)
        private val itemRating: TextView = itemView.findViewById(R.id.item_rating)

        fun bind(item: WishlistItem) {
            // Set data to views
            itemTitle.text = item.title
            itemLocation.text = item.location
            itemRating.text = item.rating.toString()

            // Here you can load the image dynamically, if needed
            // For example, using Glide or Picasso for image loading
        }
    }
}

data class WishlistItem(val title: String, val location: String, val rating: Double)
