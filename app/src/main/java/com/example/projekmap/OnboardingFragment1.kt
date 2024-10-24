package com.example.projekmap

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController


class OnboardingFragment1 : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.activity_onboarding_fragment1, container, false)

        // Set up the button click listener
        val nextButton: Button = view.findViewById(R.id.onboardingNextButton)
        nextButton.setOnClickListener {
            // Navigate to the next onboarding fragment
            findNavController().navigate(R.id.action_onboardingFragment1_to_onboardingFragment2)
        }

        return view
    }
}