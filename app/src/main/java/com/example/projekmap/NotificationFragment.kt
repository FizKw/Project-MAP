package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class NotificationFragment : Fragment() {

    private lateinit var selectedTripsRecyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_notification, container, false)

        // Initialize RecyclerView
        selectedTripsRecyclerView = view.findViewById(R.id.selectedTripsRecyclerView)

        // Set Layout Manager
        selectedTripsRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        // Dummy data for trips
        val selectedTrips = listOf(
            Trip("Borobudur Temple", "Magelang, Central Java", "2024-10-30", "$100", R.drawable.borobudur),
            Trip("Prambanan Temple", "Yogyakarta, Central Java", "2024-11-02", "$80", R.drawable.prambanan)
        )

        // Set Adapter
        val adapter = TripAdapter(selectedTrips)
        selectedTripsRecyclerView.adapter = adapter

        return view
    }
}
