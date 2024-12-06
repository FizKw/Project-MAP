package com.example.projekmap

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.button.MaterialButton
import com.google.android.material.imageview.ShapeableImageView
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
        val imageView = view.findViewById<ShapeableImageView>(R.id.profile_image)
        val updateProfileButton = view.findViewById<Button>(R.id.update_profile_button)
        val logoutButton = view.findViewById<MaterialButton>(R.id.logout_button)
        val updateVendorButton = view.findViewById<MaterialButton>(R.id.update_vendor_button)
        val updateAdminButton = view.findViewById<MaterialButton>(R.id.update_admin_button)
        updateAdminButton.visibility = View.GONE
        updateVendorButton.visibility = View.GONE


        val user = auth.currentUser
        if (user != null){
            emailView.text = user.email


            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val phoneNumber = document.getString("phone_number")
                        val profilePicture = document.getString("profile_picture")
                        val role = document.getString("role")
                        val status = document.getBoolean("status") ?: false

                        if (name.isNullOrEmpty() || phoneNumber.isNullOrEmpty()) {
                            nameView.visibility = View.GONE
                            phoneNumberView.visibility = View.GONE
                        } else {
                            nameView.text = name
                            phoneNumberView.text = phoneNumber
                            if (profilePicture != null) {
                                if (profilePicture.isNotEmpty()){
                                    Glide.with(this)
                                        .load(profilePicture)
                                        .into(imageView)
                                }
                            }
                        }
                        if (role == "vendor" && status){
                            updateVendorButton.visibility = View.VISIBLE
                        } else if(role == "admin"){
                            updateAdminButton.visibility = View.VISIBLE
                        }
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to get user Data", Toast.LENGTH_SHORT).show()
                }
        }

        updateVendorButton.setOnClickListener{
            val bundle = Bundle()
            bundle.putString("uid", auth.uid.toString())
            findNavController().navigate(R.id.edit_vendor_fragment, bundle)
        }

        updateAdminButton.setOnClickListener{
            (activity as MainActivity).hideBottomNavbar()
            findNavController().navigate(R.id.admin_page_fragment,)
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