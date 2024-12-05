package com.example.projekmap

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class AdminPageFragment : Fragment() {

//    private lateinit var adminRecyclerView: RecyclerView
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

        // Inisialisasi RecyclerView
//        adminRecyclerView = view.findViewById(R.id.adminRecyclerView)
        vendorListRecyclerView = view.findViewById(R.id.vendorRecyclerView)
        vendorValidationRecyclerView = view.findViewById(R.id.vendorvalRecyclerView)

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
}
