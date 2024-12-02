package com.example.projekmap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions


class RegisterFragment : Fragment() {

    private lateinit var auth:FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val emailInput = view.findViewById<TextInputEditText>(R.id.email_input_field)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.password_input_field)
        val phoneInput = view.findViewById<TextInputEditText>(R.id.phone_input_field)
        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        val submitButton = view.findViewById<Button>(R.id.submit_button)
        submitButton.setOnClickListener{
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            val phone = phoneInput.text.toString().trim()
            if(email.isEmpty()||password.isEmpty()||phone.isEmpty()){
                Toast.makeText(requireContext(), "Email and Password are required",
                    Toast.LENGTH_SHORT).show()
            }else{
                Log.d("email", email)
                Log.d("password", password)
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()){task->
                        if(task.isSuccessful){
                            Log.d("Register","RegisterWithEmail:success")
                            val user = auth.currentUser
                            val userData = HashMap<String, String>()
                            if (user != null) {
                                userData["name"] = user.email.toString().substringBefore("@")
                                userData["phone_number"] = phone
                                userData["profile_picture"] =""
                                userData["role"] = "user"

                                db.collection("users").document(user.uid).set(userData, SetOptions.merge())
                                    .addOnSuccessListener {
                                        Toast.makeText(requireContext(), "Registered Succesfully", Toast.LENGTH_SHORT).show()
                                    }
                                    .addOnFailureListener { e ->
                                        Toast.makeText(requireContext(), "Error updating information: ${e.message}", Toast.LENGTH_SHORT).show()
                                    }
                                findNavController().navigate(R.id.homePageFragment)
                            }

                        }else{
                            Log.w("RegisterWithEmail:failure",task.exception)
                            Toast.makeText(requireContext(), "Register Failed ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                        }
                    }

            }
        }
        view.findViewById<ImageView>(R.id.btn_back).setOnClickListener {
            findNavController().popBackStack()
        }
    }
}