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
import com.google.android.material.button.MaterialButton
import com.google.firebase.firestore.FirebaseFirestore

class AdminPageFragment : Fragment() {

    private lateinit var vendorListRecyclerView: RecyclerView
    private lateinit var vendorValidationRecyclerView: RecyclerView
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_admin_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
            findNavController().navigate(R.id.recommend_edit_fragment)
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
        setupVendorValidationRecycleView()

        setupVendorListRecycleView()

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

    private fun setupVendorValidationRecycleView(){
        val db = FirebaseFirestore.getInstance()
        val vendorValidationList = mutableListOf<VendorValidation>()

        // Query users with the role "vendor"
        db.collection("users")
            .whereEqualTo("role", "vendor")
            .get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val vendorValidation = VendorValidation(
                            uid = document.id,
                            vendor = document.getString("name") ?: "Unknown Vendor",
                            status = document.getBoolean("status") ?: false
                        )
                        vendorValidationList.add(vendorValidation)
                    }

                    // Initialize RecyclerView
                    val adapter = VendorValidationAdapter(vendorValidationList)
                    vendorValidationRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    vendorValidationRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "No vendors found.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(requireContext(), "Failed to fetch vendors: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupVendorListRecycleView(){
        val db = FirebaseFirestore.getInstance()
        val vendorList = mutableListOf<Vendor>()

        db.collection("vendors").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot != null && !snapshot.isEmpty) {
                    for (document in snapshot.documents) {
                        val vendor = Vendor(
                            uid = document.id,
                            vendor = document.getString("vendor") ?: "Unknown Vendor"
                        )
                        vendorList.add(vendor)
                    }

                    // Initialize RecyclerView
                    val adapter = VendorListAdapter(vendorList)
                    vendorListRecyclerView.layoutManager =
                        LinearLayoutManager(requireContext())
                    vendorListRecyclerView.adapter = adapter
                } else {
                    Toast.makeText(requireContext(), "No vendors found.", Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to fetch vendors: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


}
