package com.example.projekmap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.button.MaterialButton

class AdminPageFragment : Fragment() {

    private lateinit var vendorListRecyclerView: RecyclerView
    private lateinit var vendorValidationRecyclerView: RecyclerView

    private lateinit var vendorListAdapter: VendorListAdapter
    private lateinit var vendorValidationAdapter: VendorValidationAdapter

    private val vendorList = listOf(
        Vendor("Vendor 1"),
        Vendor("Vendor 2")
    )

    private val vendorValidationList = listOf(
        VendorValidation("Valid"),
        VendorValidation("Invalid")
    )

    @SuppressLint("MissingInflatedId")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_admin_page, container, false)

        // Back button logic
        view.findViewById<View>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp() // Navigasi kembali ke fragment sebelumnya
        }

        // Inisialisasi RecyclerView
        vendorListRecyclerView = view.findViewById(R.id.vendorRecyclerView)
        vendorValidationRecyclerView = view.findViewById(R.id.vendorvalRecyclerView)

        // Inisialisasi tombol Edit untuk Onboarding
        view.findViewById<MaterialButton>(R.id.edit_onboarding_button1).setOnClickListener {
            navigateToOnboardingEdit("onboarding1")
        }
        view.findViewById<MaterialButton>(R.id.edit_onboarding_button2).setOnClickListener {
            navigateToOnboardingEdit("onboarding2")
        }
        view.findViewById<MaterialButton>(R.id.edit_onboarding_button3).setOnClickListener {
            navigateToOnboardingEdit("onboarding3")
        }

        // Inisialisasi tombol Edit untuk Recommend
        view.findViewById<MaterialButton>(R.id.edit_recommended_button1).setOnClickListener {
            findNavController().navigate(R.id.action_admin_page_to_recommend_edit)
        }

        view.findViewById<MaterialButton>(R.id.edit_article_button1).setOnClickListener {
            navigateToArticleEdit("article1")
        }
        view.findViewById<MaterialButton>(R.id.edit_article_button2).setOnClickListener {
            navigateToArticleEdit("article2")
        }
        view.findViewById<MaterialButton>(R.id.edit_article_button3).setOnClickListener {
            navigateToArticleEdit("article3")
        }

        // Set layout manager untuk RecyclerView
        vendorListRecyclerView.layoutManager = LinearLayoutManager(context)
        vendorValidationRecyclerView.layoutManager = LinearLayoutManager(context)

        // Set adapter untuk Vendor List dan Vendor Validation
        vendorListAdapter = VendorListAdapter(vendorList)
        vendorValidationAdapter = VendorValidationAdapter(vendorValidationList)

        vendorListRecyclerView.adapter = vendorListAdapter
        vendorValidationRecyclerView.adapter = vendorValidationAdapter

        return view
    }

    private fun navigateToOnboardingEdit(onboardingId: String) {
        val bundle = Bundle().apply {
            putString("onboardingId", onboardingId)
        }
        findNavController().navigate(R.id.onboarding_edit_fragment, bundle)
    }

    // Fungsi untuk navigasi ke RecommendEditFragment
    private fun navigateToRecommendEdit() {
        val bundle = Bundle()
        findNavController().navigate(R.id.action_admin_page_to_recommend_edit, bundle)
    }

    private fun navigateToArticleEdit(articleId: String) {
        val bundle = Bundle().apply {
            putString("articleId", articleId)
        }
        findNavController().navigate(R.id.article_edit_fragment, bundle)
    }


}
