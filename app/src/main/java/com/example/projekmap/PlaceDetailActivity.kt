package com.example.projekmap

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class PlaceDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_place_detail)

        // Ambil data dari Intent
        val placeName = intent.getStringExtra("PLACE_NAME")
        val placeLocation = intent.getStringExtra("PLACE_LOCATION")
        val placeRating = intent.getStringExtra("PLACE_RATING")
        val placeImageRes = intent.getIntExtra("PLACE_IMAGE", 0)
        val placeDescription = intent.getStringExtra("PLACE_DESCRIPTION") // Ambil deskripsi

        // Set data ke Views
        findViewById<TextView>(R.id.place_name).text = placeName
        findViewById<TextView>(R.id.place_location).text = placeLocation
        findViewById<TextView>(R.id.place_description).text = placeDescription
        findViewById<ImageView>(R.id.place_image).setImageResource(placeImageRes)
        findViewById<TextView>(R.id.place_rating).text = placeRating

        // Handle back button
        val backButton = findViewById<ImageView>(R.id.back_button)
        backButton.setOnClickListener {
            onBackPressed()  // Kembali ke halaman sebelumnya (home)
        }
    }
}
