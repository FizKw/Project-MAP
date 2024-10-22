package com.example.projekmap

import android.os.Bundle
import android.view.View
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var bottomNavbar: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.container) as NavHostFragment
        val navController = navHostFragment.navController
        bottomNavbar = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        bottomNavbar?.setupWithNavController(navController)
        bottomNavbar.visibility = View.VISIBLE
    }

    fun hideBottomNavbar(){
        bottomNavbar.visibility = View.GONE
    }
    fun showBottomNavbar(){
        bottomNavbar.visibility = View.VISIBLE
    }
}