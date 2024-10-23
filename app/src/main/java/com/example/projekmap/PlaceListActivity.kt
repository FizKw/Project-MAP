package com.example.projekmap

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class PlaceListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_list)

        // Setup toolbar with back navigation
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)  // Enable back button on toolbar

        // Set click listener for back button
        toolbar.setNavigationOnClickListener {
            onBackPressed()  // Navigate back when button is clicked
        }

        // Ambil wilayah dari Intent
        val region = intent.getStringExtra("REGION")

        // Temukan RecyclerView
        val placeRecyclerView = findViewById<RecyclerView>(R.id.placeRecyclerView)

        // Setup RecyclerView
        placeRecyclerView.layoutManager = LinearLayoutManager(this)

        // Tampilkan tempat berdasarkan wilayah yang dipilih
        val places = when (region) {
            "Central Java" -> getCentralJavaPlaces()
            "Yogyakarta" -> getYogyakartaPlaces()
            else -> emptyList()
        }

        // Set adapter
        val adapter = PlaceAdapter(places)
        placeRecyclerView.adapter = adapter
    }

    private fun getCentralJavaPlaces(): List<Place> {
        return listOf(
            Place("Borobudur Temple", "Magelang, Central Java", 4.8, R.drawable.borobudur, "Ancient Buddhist temple."),
            Place("Prambanan Temple", "Yogyakarta, Central Java", 4.7, R.drawable.prambanan, "Hindu temple complex.")
        )
    }

    private fun getYogyakartaPlaces(): List<Place> {
        return listOf(
            Place("Malioboro Street", "Yogyakarta", 4.5, R.drawable.malioboro, "Famous street in Yogyakarta."),
            Place("Tugu Jogja", "Yogyakarta", 4.6, R.drawable.tugu_jogja, "Iconic monument in Yogyakarta.")
        )
    }
}
