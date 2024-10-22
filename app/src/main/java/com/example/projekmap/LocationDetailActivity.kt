package com.example.projekmap

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class LocationDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_location_detail)

        // Ambil data dari Intent
        val locationName = intent.getStringExtra("LOCATION_NAME")
        val locationImage = intent.getIntExtra("LOCATION_IMAGE", 0)
        val locationDescription = intent.getStringExtra("LOCATION_DESCRIPTION")

        // Set data ke Views
        findViewById<TextView>(R.id.location_name).text = locationName
        findViewById<ImageView>(R.id.location_image).setImageResource(locationImage)
        findViewById<TextView>(R.id.location_description).text = locationDescription
    }
}
