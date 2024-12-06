package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class RecommendEditFragment : Fragment() {
    private lateinit var recommendRecyclerView: RecyclerView
    private lateinit var saveButton: Button
    private lateinit var adapter: RecommendEditAdapter
    private lateinit var db: FirebaseFirestore
    private val recommendList = mutableListOf<Recommend>()
    private val selectedItems = mutableListOf<String>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recommend_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize RecyclerView
        recommendRecyclerView = view.findViewById(R.id.recommendEditRecyclerview)
        saveButton = view.findViewById(R.id.save_button)
        recommendRecyclerView.layoutManager = LinearLayoutManager(context)

        // Fetch vendors and previously selected recommendations
        fetchVendors()

        saveButton.setOnClickListener {
            saveRecommendations()
        }

        // Back button listener
        val backButton: ImageView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun fetchVendors() {
        db = FirebaseFirestore.getInstance()

        // Fetch previously selected recommendations
        db.collection("recommend").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    selectedItems.clear()
                    snapshot.documents.forEach { document ->
                        document.getString("vendorId")?.let { selectedItems.add(it) }
                    }
                }

                // Fetch all vendors
                fetchAllVendors()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch previous recommendations", Toast.LENGTH_SHORT).show()
                fetchAllVendors() // Still fetch vendors even if recommendations fail
            }
    }

    private fun fetchAllVendors() {
        db.collection("vendors").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null) {
                    recommendList.clear()
                    for (doc in snapshot.documents) {
                        val id = doc.id
                        val vendor = doc.getString("vendor") ?: "Unknown Vendor"
                        val place = doc.getString("place") ?: "Unknown Place"

                        recommendList.add(Recommend(id, vendor, place))
                    }

                    // Set up the adapter with fetched vendors and pre-selected items
                    adapter = RecommendEditAdapter(recommendList, selectedItems)
                    recommendRecyclerView.adapter = adapter
                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch vendors", Toast.LENGTH_SHORT).show()
            }
    }

    private fun saveRecommendations() {
        if (selectedItems.size != 3) {
            Toast.makeText(requireContext(), "You must select exactly 3 items", Toast.LENGTH_SHORT).show()
            return
        }

        val batch = db.batch()
        val recommendationCollection = db.collection("recommend")

        selectedItems.forEachIndexed { index, vendorId ->
            val docRef = recommendationCollection.document("recommendation${index + 1}")
            val data = mapOf("vendorId" to vendorId)
            batch.set(docRef, data)
        }

        batch.commit()
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Recommendations saved successfully!", Toast.LENGTH_SHORT).show()
                findNavController().navigateUp()
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Error saving recommendations: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }
}




