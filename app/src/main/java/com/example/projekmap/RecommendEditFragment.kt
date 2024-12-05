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

class RecommendEditFragment : Fragment() {
    private lateinit var recommendRecyclerView: RecyclerView // Pastikan nama variabel jelas

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Muat layout fragment_recommend_edit.xml
        val view = inflater.inflate(R.layout.fragment_recommend_edit, container, false)

        // Inisialisasi RecyclerView
        recommendRecyclerView = view.findViewById(R.id.RecommendEditRecyclerview)

        // Atur layout manager untuk RecyclerView
        recommendRecyclerView.layoutManager = LinearLayoutManager(context)

        // Set adapter untuk RecyclerView (pastikan adapter Anda sudah benar)
        val dummyRecommendList = listOf(
            Recommend("Recommend 1"),
            Recommend("Recommend 2"),
            Recommend("Recommend 3")
        )
        val adapter = RecommendEditAdapter(dummyRecommendList)
        recommendRecyclerView.adapter = adapter

        // Tambahkan listener untuk tombol back
        val backButton: ImageView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().navigateUp() // Navigasi kembali
        }

        return view
    }
}
