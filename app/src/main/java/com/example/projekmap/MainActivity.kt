package com.example.projekmap

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    lateinit var bottomNavigationView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.setupWithNavController(navController)

        // Add this: Check if the user has already logged in or completed onboarding
        val sharedPref = getSharedPreferences("user_prefs", Context.MODE_PRIVATE)
        val isLoggedIn = sharedPref.getBoolean("isLoggedIn", false)
        val isOnboardingCompleted = sharedPref.getBoolean("isOnboardingCompleted", false)

        // Navigate based on the login and onboarding status
        if (isLoggedIn) {
            // User is already logged in, navigate to home screen
            navController.navigate(R.id.homePageFragment)
        } else if (isOnboardingCompleted) {
            // Onboarding is completed, navigate to login screen
            navController.navigate(R.id.loginFragment)
        } else {
            // Show onboarding if neither condition is met
            navController.navigate(R.id.onboardingFragment)
        }

        // Manage BottomNavigation visibility based on the current fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onboardingFragment, R.id.onboardingFragment1, R.id.onboardingFragment2,
                R.id.loginFragment, R.id.registerFragment, R.id.edit_vendor_fragment,
                    R.id.onboarding_edit_fragment, R.id.article_edit_fragment, R.id.recommend_edit_fragment-> hideBottomNavbar()
                else -> showBottomNavbar()
            }
        }
    }

    // Function to hide the bottom navigation bar
    fun hideBottomNavbar() {
        bottomNavigationView.visibility = View.GONE
    }

    // Function to show the bottom navigation bar
    fun showBottomNavbar() {
        bottomNavigationView.visibility = View.VISIBLE
    }
}
