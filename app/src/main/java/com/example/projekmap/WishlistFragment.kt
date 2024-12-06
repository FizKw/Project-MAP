package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.UUID
import kotlin.random.Random

class WishlistFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var placeAdapter: PlaceAdapter
    private lateinit var db : FirebaseFirestore
    private lateinit var auth : FirebaseAuth


    private val favoritePlaces = mutableListOf<Place>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wishlist, container, false)


    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Initialize RecyclerView
        recyclerView = view.findViewById(R.id.wishlist)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Initialize PlaceAdapter
        placeAdapter = PlaceAdapter(favoritePlaces)
        recyclerView.adapter = placeAdapter

        fetchFavorites()


    }

    private fun fetchFavorites() {
        db = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()
        val favoritesRef = db.collection("users").document(auth.uid.toString()).collection("favorites")

        favoritesRef.get().addOnSuccessListener { snapshot ->
            if (snapshot != null && !snapshot.isEmpty) {
                val placeIds = snapshot.documents.mapNotNull { it.id }

                fetchPlaceDetails(placeIds)
            } else {
                Toast.makeText(requireContext(), "No favorites found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(requireContext(), "Failed to fetch favorites", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchPlaceDetails(placeIds: List<String>){
        db = FirebaseFirestore.getInstance()
        favoritePlaces.clear()

        placeIds.forEach { placeId ->
            db.collection("vendors").document(placeId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val place = Place(
                            id = document.id,
                            vendor = document.getString("vendor") ?: "Unknown Vendor",
                            place = document.getString("place") ?: "Unknown Place",
                            desc = document.getString("desc") ?: "No Description",
                            avgRating = document.getDouble("avg_rating") ?: 0.0,
                            vendorImage = document.getString("vendor_image") ?: "",
                            estimate = document.getString("estimate") ?: "",
                            type = document.getString("type") ?: "",
                            via = document.getString("via") ?: ""
                        )
                        favoritePlaces.add(place)
                        placeAdapter.notifyDataSetChanged()
                    }
                }. addOnFailureListener{
                    Toast.makeText(requireContext(), "Failed to fetch place details", Toast.LENGTH_SHORT).show()
                }
        }

    }
}
