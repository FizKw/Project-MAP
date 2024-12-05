package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class OnboardingEditFragment : Fragment() {

    private lateinit var onboardingId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            onboardingId = it.getString("onboardingId", "")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_onboarding_edit, container, false)

        // Tambahkan listener untuk tombol back
        val backButton: ImageView = view.findViewById(R.id.back_button)
        backButton.setOnClickListener {
            findNavController().navigateUp() // Navigasi kembali
        }

        return view
    }
}
