package com.example.projekmap

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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val emailView = view.findViewById<TextView>(R.id.email_view)
        val nameView = view.findViewById<TextView>(R.id.name_view)
        val phoneNumberView = view.findViewById<TextView>(R.id.phone_number_view)
//        TODO Profile Picture
//        val imageView = view.findViewById<ImageView>(R.id.image_view)
        val updateProfileButton = view.findViewById<Button>(R.id.update_profile_button)
        val logoutButton = view.findViewById<Button>(R.id.logout_button)

        val user = auth.currentUser
        if (user != null){
            emailView.text = user.email

            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val phoneNumber = document.getString("phone_number")
//                        TODO Profile Picture
//                        val profile_picture = document.getString("profile_picture")

                        if (name.isNullOrEmpty() || phoneNumber.isNullOrEmpty()) {
                            nameView.visibility = View.GONE
                            phoneNumberView.visibility = View.GONE
//                            TODO Profile Picture
//                            imageView.visibility = View.GONE
                        } else {
                            nameView.text = name
                            phoneNumberView.text = phoneNumber
//                            TODO Profile Picture
//                            Glide.with(this)
//                                .load(profile_picture)
//                                .into(imageView)
                        }
                    }
                }
        }

        updateProfileButton.setOnClickListener {
            findNavController().navigate(R.id.updateProfileFragment)
        }

        logoutButton.setOnClickListener {
            auth.signOut()
            (activity as MainActivity).hideBottomNavbar()
            findNavController().navigate(R.id.loginFragment)
        }
    }
}