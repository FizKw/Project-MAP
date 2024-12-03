package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class WishlistFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_wishlist, container, false)

        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.wishlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize PlaceAdapter
        placeAdapter = PlaceAdapter(getSamplePlaces())
        recyclerView.adapter = placeAdapter

        return view
    }


    // Provide sample data for testing
    private fun getSamplePlaces(): List<Place> {
        return listOf(
            Place(
                name = "Semeru Mountain",
                location = "East Java, Indonesia",
                description = "Semeru Mountain is The highest Volcano in Java.",
                rating = 4.8,
                imageRes = R.drawable.semeru
            ),
            Place(
                name = "Borobudur",
                location = "Jauh",
                description = "A stunning natural wonder in Jawa, great for exploring and photography.",
                rating = 4.9,
                imageRes = R.drawable.borobudur
            ),
            Place(
                name = "Malioboro",
                location = "Jauh",
                description = "A famous vacation in Jawa, a must-visit for sightseeing and romance.",
                rating = 4.7,
                imageRes = R.drawable.malioboro
            )
        )
    }

}
