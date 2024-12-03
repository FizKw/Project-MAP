package com.example.projekmap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecommendEditFragment {
    private lateinit var RecommendEditFragmentRecyclerView: RecyclerView

    @SuppressLint("MissingInflatedId")
    fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_page, container, false)

        RecommendEditFragmentRecyclerView = view.findViewById(R.id.RecommendEditRecyclerview)

        return view
    }
}