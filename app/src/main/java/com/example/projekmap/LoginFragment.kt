package com.example.projekmap

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.SignInAccount
import com.google.android.gms.auth.api.signin.internal.GoogleSignInOptionsExtensionParcelable
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FirebaseFirestore

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private val googleSignInRequestCode = 234


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
        val googleButton = view.findViewById<SignInButton>(R.id.google_sign_in_button)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()

        registerButton.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }
        val role = arguments?.getString("role")
        if (role == "vendor"){
            googleButton.visibility = View.GONE
        }

        val loginButton = view.findViewById<Button>(R.id.login_button)
        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(requireContext(), "Email and Password are required",
                    Toast.LENGTH_SHORT).show()
            } else {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Log.d("Login", "signInWithEmail:success")

                            // Add this: Save login state in SharedPreferences
                            val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                            sharedPref.edit().putBoolean("isLoggedIn", true).apply()

                            // Show the BottomNavigationView and navigate to home page
                            (activity as MainActivity).showBottomNavbar()
                            findNavController().navigate(R.id.homePageFragment)
                        } else {
                            Log.w("Login", "signInWithEmail:failure", task.exception)
                            Toast.makeText(requireContext(), "Email and Password Wrong",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireActivity(), gso)

        googleButton.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            startActivityForResult(signInIntent, googleSignInRequestCode)

        }



        registerButton.setOnClickListener {
            findNavController().navigate(R.id.registerFragment)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when(requestCode){
            googleSignInRequestCode -> {
                try {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    val account = task.getResult(ApiException::class.java)
                    firebaseAuthWithGoogle(account)

                    Log.d("Google Sign-In", "firebaseAuthWithGoogle:" + account.id)

                } catch (e: Exception) {
                    Log.w("Google Sign-In", "Google sign in failed", e)
                    Toast.makeText(requireContext(), "Google Sign-In failed 1",
                        Toast.LENGTH_SHORT).show()
                }
            }

        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount){
        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        FirebaseAuth.getInstance().signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    Log.d("Google Sign-In", "signInWithCredential:success")

                    // Add this: Save login state in SharedPreferences
                    val sharedPref = requireActivity().getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putBoolean("isLoggedIn", true).apply()

                    // Show the BottomNavigationView and navigate to home page
                    (activity as MainActivity).showBottomNavbar()
                    findNavController().navigate(R.id.homePageFragment)
                }
            }
            .addOnFailureListener {
                it.printStackTrace()
                Log.w("Google Sign-In", "signInWithCredential:failure", it)
                Toast.makeText(requireContext(), "Google Sign-In failed 2",
                    Toast.LENGTH_SHORT).show()
            }
    }

}
