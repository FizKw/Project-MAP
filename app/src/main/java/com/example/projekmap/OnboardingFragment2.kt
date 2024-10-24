package com.example.projekmap

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class OnboardingFragment2 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_onboarding_fragment2, container, false)

        // Set up the button click listener
        val nextButton: Button = view.findViewById(R.id.onboardingNextButton)
        nextButton.setOnClickListener {
            // Save onboarding completion status in SharedPreferences
            val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("isOnboardingCompleted", true).apply()

            // Navigate to the login fragment
            findNavController().navigate(R.id.action_onboardingFragment2_to_loginFragment)
        }

        return view
    }
}
