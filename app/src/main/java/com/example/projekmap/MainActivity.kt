package com.example.projekmap

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

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.onboardingFragment, R.id.onboardingFragment1, R.id.onboardingFragment2,
                R.id.loginFragment, R.id.registerFragment -> hideBottomNavbar()
                else -> showBottomNavbar()
            }
        }
    }

    // Ubah visibilitas dari private menjadi public atau default
    fun hideBottomNavbar() {
        bottomNavigationView.visibility = View.GONE
    }

    fun showBottomNavbar() {
        bottomNavigationView.visibility = View.VISIBLE
    }
}

