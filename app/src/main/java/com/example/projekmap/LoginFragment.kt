package com.example.projekmap

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth

class LoginFragment : Fragment() {

    private lateinit var auth:FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).hideBottomNavbar()
        val registerButton = view.findViewById<Button>(R.id.register_button)

        val emailInput = view.findViewById<TextInputEditText>(R.id.email_input_field)
        val passwordInput = view.findViewById<TextInputEditText>(R.id.password_input_field)
        auth = FirebaseAuth.getInstance()

        registerButton.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }

        val loginButton = view.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener{
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if(email.isEmpty()||password.isEmpty()){
                Toast.makeText(requireContext(), "Email and Password are required",
                    Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email,password)
                    .addOnCompleteListener(requireActivity()){task->
                        if(task.isSuccessful){
                            Log.d("Login", "signInWithEmail:success")
                            (activity as MainActivity).showBottomNavbar()
                            findNavController().navigate(R.id.homePageFragment)
                        }else{
                            Log.w("Login", "signInWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Email and Password Wrong",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        registerButton.setOnClickListener{
            findNavController().navigate(R.id.registerFragment)
        }
    }



    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment loginFragment.
         */
        // TODO: Rename and change types and number of parameters


    }
}