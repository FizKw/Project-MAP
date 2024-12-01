package com.example.projekmap

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class PlaceDetailActivity : AppCompatActivity() {

    private var isFavorite: Boolean = false // Menyimpan status favorit

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

        val backButton: ImageView = findViewById(R.id.back_button)
        val favoriteButton: ImageView = findViewById(R.id.favorite_button)

        // Listener untuk tombol kembali
        backButton.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }

        // Listener untuk tombol favorite
        favoriteButton.setOnClickListener {
            isFavorite = !isFavorite // Toggle status favorite
            updateFavoriteIcon(favoriteButton) // Perbarui ikon love

            // Tampilkan pesan kepada pengguna
            val message = if (isFavorite) "Added to favorites" else "Removed from favorites"
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        }
    }

    // Fungsi untuk memperbarui ikon berdasarkan status favorite
    private fun updateFavoriteIcon(favoriteButton: ImageView) {
        if (isFavorite) {
            favoriteButton.setImageResource(R.drawable.ic_lovefull) // Ikon love terisi
        } else {
            favoriteButton.setImageResource(R.drawable.ic_love) // Ikon love kosong
        }
    }
}
