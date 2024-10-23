package com.example.projekmap

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.cardview.widget.CardView

class WishlistFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        // Referensi ke CardView untuk hiking dan travel
        val hikingCard = view.findViewById<CardView>(R.id.hikingWishlistCard)
        val travelCard = view.findViewById<CardView>(R.id.travelWishlistCard)

        // Listener untuk hiking
        hikingCard?.setOnClickListener {
            val intent = Intent(requireContext(), WishlistDetailActivity::class.java)
            intent.putExtra("wishlist_type", "hiking")  // Kirim data tipe wishlist
            startActivity(intent)
        }

        // Listener untuk travel
        travelCard?.setOnClickListener {
            val intent = Intent(requireContext(), WishlistDetailActivity::class.java)
            intent.putExtra("wishlist_type", "travel")  // Kirim data tipe wishlist
            startActivity(intent)
        }

        return view
    }
}
