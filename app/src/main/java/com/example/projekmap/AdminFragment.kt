package com.example.projekmap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminPageFragment : Fragment() {

    private lateinit var adminRecyclerView: RecyclerView
    private lateinit var onboardingRecyclerView: RecyclerView
    private lateinit var PopularRecyclerView: RecyclerView
    private lateinit var RecommendRecyclerView: RecyclerView
    private lateinit var ArticleRecyclerView: RecyclerView



    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_page, container, false)

        adminRecyclerView = view.findViewById(R.id.adminRecyclerView)

        return view
    }
}
