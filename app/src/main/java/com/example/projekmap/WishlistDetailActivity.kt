package com.example.projekmap

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WishlistDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.wishlist_type)  // Layout untuk tampilan tipe wishlist

        // Ambil data tipe wishlist dari Intent
        val wishlistType = intent.getStringExtra("wishlist_type") ?: "Unknown"

        // Atur title sesuai dengan tipe wishlist yang dipilih
        val titleTextView = findViewById<TextView>(R.id.wishlist_detail_title)
        titleTextView.text = "Wishlist ($wishlistType)"

        // Inisialisasi RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.wishlist)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Load data berdasarkan tipe wishlist
        val items = if (wishlistType == "hiking") {
            listOf(
                WishlistItem("Semeru Mountain", "Malang, East Java", 4.8),
                WishlistItem("Arjuno Mountain", "Malang, East Java", 4.5),
                WishlistItem("Prau Mountain", "Dieng, Central Java", 4.6)
            )
        } else {
            listOf(
                WishlistItem("Bali Beach", "Bali", 4.7),
                WishlistItem("Central Java Tour", "Central Java", 4.5),
                WishlistItem("Karimunjawa", "Jepara, Central Java", 4.8)
            )
        }

        // Set adapter ke RecyclerView
        recyclerView.adapter = WishlistAdapter(wishlistType, items)
    }
}
