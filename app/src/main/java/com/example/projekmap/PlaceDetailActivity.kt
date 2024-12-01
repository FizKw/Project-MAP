package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

        // Handle tombol Rate This Place
        val rateButton = findViewById<Button>(R.id.btn_rate)
        rateButton.setOnClickListener {
            showRatingDialog()
        }
    }

    private fun showRatingDialog() {
        // Inflate layout dialog_rate_place
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_rate_place, null)

        // Buat AlertDialog
        val dialog = AlertDialog.Builder(this)
            .setView(dialogView)
            .create()

        // Temukan RatingBar dan Button dari layout dialog
        val ratingBar = dialogView.findViewById<RatingBar>(R.id.rating_bar)
        val submitButton = dialogView.findViewById<Button>(R.id.btn_submit_rating)

        // Handle klik tombol Submit
        submitButton.setOnClickListener {
            val rating = ratingBar.rating
            Toast.makeText(this, "You rated $rating stars", Toast.LENGTH_SHORT).show()
            dialog.dismiss() // Tutup dialog setelah submit
        }

        // Tampilkan dialog
        dialog.show()
    }
}
