package com.example.projekmap

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore

class OnboardingFragment2 : Fragment() {

    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_onboarding_fragment2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = FirebaseFirestore.getInstance()

        val nextButton: Button = view.findViewById(R.id.onboardingNextButton)
        nextButton.setOnClickListener {
            // Save onboarding completion status in SharedPreferences
            val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
            sharedPref.edit().putBoolean("isOnboardingCompleted", true).apply()

            // Navigate to the login fragment
            findNavController().navigate(R.id.action_onboardingFragment2_to_loginFragment)
        }
        db.collection("onboarding").document("onboarding3").get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val imageUrl = document.getString("imageUrl")
                    view.findViewById<TextView>(R.id.onboardingTitle).text = document.getString("title")
                    view.findViewById<TextView>(R.id.onboardingDescription).text = document.getString("description")
                    Glide.with(requireContext())
                        .load(imageUrl)
                        .into(view.findViewById<ImageView>(R.id.onboardingImage))
                }
            }
    }
}
