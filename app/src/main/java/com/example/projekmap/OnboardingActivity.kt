package com.example.projekmap

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator

class OnboardingActivity : AppCompatActivity() {

    private lateinit var viewPager: ViewPager2
    private lateinit var onboardingAdapter: OnboardingAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding)

        viewPager = findViewById(R.id.viewPager)
        onboardingAdapter = OnboardingAdapter(this)

        viewPager.adapter = onboardingAdapter

        // Optional: Tab indicators (if you want to show page indicators)
        val tabLayout = findViewById<com.google.android.material.tabs.TabLayout>(R.id.tabLayout)
        TabLayoutMediator(tabLayout, viewPager) { _, _ -> }.attach()
    }
}
