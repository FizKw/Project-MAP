package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment

class PaymentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate layout fragment_payment
        val view = inflater.inflate(R.layout.fragment_payment, container, false)

        // Tombol Next
        val nextButton = view.findViewById<Button>(R.id.next_button)
        nextButton.setOnClickListener {
            navigateToFragment(SuksesFragment())
        }

        return view
    }

    private fun navigateToFragment(fragment: Fragment) {
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(android.R.id.content, fragment) // Ganti layout utama dengan SuccessFragment
            .addToBackStack(null)
            .commit()
    }
}
