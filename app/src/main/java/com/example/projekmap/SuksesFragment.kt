package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class SuksesFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for SuccessFragment
        val view = inflater.inflate(R.layout.fragment_sukses, container, false)

        // Tombol untuk kembali ke Home
        val backToHomeButton = view.findViewById<Button>(R.id.balik_keawal_button)
        backToHomeButton.setOnClickListener {
            requireActivity().finish() // Menutup aktivitas dan kembali ke home
        }

        return view
    }
}
