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
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.math.RoundingMode

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var db: FirebaseFirestore
    private lateinit var auth: FirebaseAuth
    private var isFavorite: Boolean = false // Menyimpan status favorit
    private var placeId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        setContentView(R.layout.activity_place_detail)

        // Ambil data dari Intent
        placeId = intent.getStringExtra("PLACE_ID").toString()
        val placeName = intent.getStringExtra("PLACE_NAME")
        val placeLocation = intent.getStringExtra("PLACE_LOCATION")
        val avgRating = intent.getStringExtra("PLACE_RATING")
        val placeImageRes = intent.getStringExtra("PLACE_IMAGE")
        val placeDescription = intent.getStringExtra("PLACE_DESCRIPTION") // Ambil deskripsi
        val placeType = intent.getStringExtra("PLACE_TYPE")
        val placeEstimate = intent.getStringExtra("PLACE_ESTIMATE")
        val placeVia = intent.getStringExtra("PLACE_VIA")

        // Set data ke Views
        findViewById<TextView>(R.id.place_name).text = placeName
        findViewById<TextView>(R.id.place_location).text = placeLocation
        findViewById<TextView>(R.id.place_description).text = placeDescription
        Glide.with(this).load(placeImageRes).into(findViewById<ImageView>(R.id.place_image))
//        findViewById<ImageView>(R.id.place_image).setImageResource(placeImageRes)
        findViewById<TextView>(R.id.place_rating).text = avgRating
        findViewById<TextView>(R.id.place_type).text = placeType
        findViewById<TextView>(R.id.place_estimate).text = placeEstimate
        findViewById<TextView>(R.id.place_via).text = placeVia

        val backButton: ImageView = findViewById(R.id.back_button)
        val favoriteButton: ImageView = findViewById(R.id.favorite_button)

        // Listener untuk tombol kembali
        backButton.setOnClickListener {
            finish() // Kembali ke aktivitas sebelumnya
        }

        checkFavoriteStatus(favoriteButton)

        // Listener untuk tombol favorite
        favoriteButton.setOnClickListener {
            val userFavoritesRef = db.collection("users").document(auth.uid.toString()).collection("favorites").document(placeId)


            isFavorite = !isFavorite // Toggle status favorite
            updateFavoriteIcon(favoriteButton) // Perbarui ikon love

            if (isFavorite){
                val favoriteData = hashMapOf(
                    "place_id" to placeId,
                    "place_name" to placeName,
                )
                userFavoritesRef.set(favoriteData)
                    .addOnSuccessListener {
                        Toast.makeText(this, "Added to favorites", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to add to favorites", Toast.LENGTH_SHORT).show()
                    }
            } else {
                userFavoritesRef.delete()
                    .addOnSuccessListener {
                        Toast.makeText(this, "Removed from favorites", Toast.LENGTH_SHORT).show()
                    }
                    .addOnFailureListener {
                        Toast.makeText(this, "Failed to remove from favorites", Toast.LENGTH_SHORT).show()
                    }
            }
        }

        // Handle tombol Rate This Place
        val rateButton = findViewById<Button>(R.id.btn_rate)
        rateButton.setOnClickListener {
            showRatingDialog()
        }
    }

    private fun checkFavoriteStatus(favoriteButton: ImageView) {
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        if (auth.currentUser != null){
            val userFavoriteRef = db.collection("users").document(auth.uid.toString()).collection("favorites").document(placeId)
            userFavoriteRef.get()
                .addOnSuccessListener { document ->
                    isFavorite = document.exists()
                    updateFavoriteIcon(favoriteButton)
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed to get favorite status", Toast.LENGTH_SHORT).show()
                }
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

        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        val docRef = db.collection("vendors").document(placeId)
        val ratingRef = docRef.collection("ratings").document(auth.currentUser?.uid.toString())

        ratingRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val previousRating = document.getDouble("rating") ?: 0.0
                    ratingBar.rating = previousRating.toFloat()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to get rating", Toast.LENGTH_SHORT).show()
            }

        // Handle klik tombol Submit
        submitButton.setOnClickListener {
            val newRating = ratingBar.rating

            val ratingData = hashMapOf(
                "rating" to newRating
            )

            ratingRef.set(ratingData)
                .addOnSuccessListener {
                    docRef.collection("ratings").get()
                        .addOnSuccessListener { snapshot ->
                            if (snapshot != null && !snapshot.isEmpty){
                                val ratings = snapshot.documents.mapNotNull { it.getDouble("rating") }
                                val avgRating = ratings.average().toBigDecimal().setScale(1, RoundingMode.HALF_UP).toDouble()

                                docRef.update("avg_rating", avgRating)
                                    .addOnSuccessListener {
                                        Toast.makeText(this, "Rating submitted", Toast.LENGTH_SHORT).show()
                                        findViewById<TextView>(R.id.place_rating).text = avgRating.toString()

                                    }
                                    .addOnFailureListener {
                                        Toast.makeText(this, "Failed to update average rating", Toast.LENGTH_SHORT).show()
                                    }
                            }

                        }.addOnFailureListener {
                            Toast.makeText(this, "Failed to get ratings", Toast.LENGTH_SHORT).show()
                        }
                }.addOnFailureListener {
                    Toast.makeText(this, "Failed to submit rating", Toast.LENGTH_SHORT).show()
                }

            Toast.makeText(this, "You rated $newRating stars", Toast.LENGTH_SHORT).show()
            dialog.dismiss() // Tutup dialog setelah submit
        }

        // Tampilkan dialog
        dialog.show()
    }
}
