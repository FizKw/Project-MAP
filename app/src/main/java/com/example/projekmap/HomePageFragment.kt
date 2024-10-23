package com.example.projekmap

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.health.connect.datatypes.ExerciseRoute
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import android.media.tv.AdRequest
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import java.io.IOException
import java.util.Locale

class HomePageFragment : BaseAuthFragment() {

    private lateinit var popularNearbyRecyclerView: RecyclerView
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var nearestLocationRecyclerView: RecyclerView
    private lateinit var chooseLocationRecyclerView: RecyclerView
    private lateinit var requestPermissionlauncher: ActivityResultLauncher<String>
    private lateinit var mMap: GoogleMap
//    private lateinit var geocoder: Geocoder
    private lateinit var locationText: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home_page, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireContext())
        locationText = view.findViewById(R.id.location_now_text)

        // Initialize RecyclerView using findViewById
        popularNearbyRecyclerView = view.findViewById(R.id.popularNearbyRecyclerView)
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView)
        nearestLocationRecyclerView = view.findViewById(R.id.nearestLocationRecyclerView)
        chooseLocationRecyclerView = view.findViewById(R.id.chooseLocationRecyclerView) // Add Choose Location RecyclerView



    if (hasLocationPermission()){
        getLocation()
    }else{
        ActivityCompat.requestPermissions(
            requireActivity(),
            arrayOf(ACCESS_FINE_LOCATION, ACCESS_COARSE_LOCATION),
            100
        )
        getLocation()
    }

        // Setup RecyclerView
        setupPopularNearbyRecyclerView()
        setupRecommendedRecyclerView()
        setupNearestLocationRecyclerView()
        setupChooseLocationRecyclerView() // Add setup for Choose Location
    }

    private fun hasLocationPermission() = arrayOf(
        ACCESS_FINE_LOCATION,
        ACCESS_COARSE_LOCATION
    ).all {
        ContextCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
    }



    @SuppressLint("MissingPermission")
    private fun getLocation(){
        fusedLocationProviderClient.lastLocation
            .addOnSuccessListener {
                location: android.location.Location? -> location?.let{
                val latLng = LatLng(location.latitude, location.longitude)
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                try {
                    val addressList: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                    if (addressList != null && addressList.isNotEmpty()) {
                        val address = addressList[0]
                        val sb = StringBuilder()
                        for (i in 0 until address.maxAddressLineIndex) {
                            sb.append(address.getAddressLine(i)).append("\n")
                        }
                        if (address.premises != null)
                            sb.append(address.premises).append(", ")
                        sb.append(address.subAdminArea).append(",")
                        sb.append(address.adminArea)
                        locationText.text = sb.toString()

                    }
                }catch (e: IOException){
                    Log.e("Error", e.message.toString())
                    Toast.makeText(requireContext(), "Error: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }




    private fun showPermissionRationale(positiveAction: () -> Unit){
        AlertDialog.Builder(requireContext())
            .setTitle("Location Persmission")
            .setMessage("This app will not work without knowing your current location")
            .setPositiveButton(android.R.string.ok) {_, _ -> positiveAction()}
            .setNegativeButton(android.R.string.cancel) {dialog, _ ->dialog.dismiss()}
            .create().show()
    }


    // Setup for Popular Nearby RecyclerView
    private fun setupPopularNearbyRecyclerView() {
        // Dummy data for Popular Nearby
        val popularNearbyPlaces = listOf(
            Place("Semeru Mountain", "East Java, Indonesia", 4.5, R.drawable.semeru, "Semeru Mountain is the highest volcano in Java."),
            Place("Raja Ampat", "West Papua, Indonesia", 4.7, R.drawable.raja_ampat, "Raja Ampat is famous for its stunning marine biodiversity."),
            Place("Bali Island", "Bali, Indonesia", 4.8, R.drawable.bali, "Bali is a popular tourist destination known for its beaches and temples.")
        )

        // Setup RecyclerView
        val adapter = PopularNearbyAdapter(popularNearbyPlaces)
        popularNearbyRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        popularNearbyRecyclerView.adapter = adapter
    }

    // Setup for Recommended RecyclerView
    private fun setupRecommendedRecyclerView() {
        // Dummy data for Recommended
        val recommendedPlaces = listOf(
            Place("Kelimutu Mountain", "Flores, NTT", 4.3, R.drawable.kelimutu, "Kelimutu is famous for its three colored lakes."),
            Place("Karimun Jawa Island", "Jepara, Central Java", 4.6, R.drawable.karimunjawa, "Karimunjawa is a tropical paradise with clear water."),
            Place("Pahawang Island", "Lampung, Indonesia", 4.5, R.drawable.pahawang, "Pahawang is a hidden gem with pristine beaches.")
        )

        // Setup RecyclerView
        val adapter = RecommendedAdapter(recommendedPlaces)
        recommendedRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recommendedRecyclerView.adapter = adapter
    }

    // Setup for Nearest Location RecyclerView
    private fun setupNearestLocationRecyclerView() {
        // Dummy data for Nearest Location
        val nearestPlaces = listOf(
            Place("Pramuka Island", "Thousand Island, Jakarta Kep", 4.5, R.drawable.pramuka_island, "Pramuka Island is famous for its pristine beaches."),
            Place("Pari Island", "Thousand Island, Jakarta Kep", 4.3, R.drawable.pari_island, "Pari Island is a beautiful island with clear waters and great snorkeling spots.")
        )

        // Setup RecyclerView
        val adapter = NearestLocationAdapter(nearestPlaces)
        nearestLocationRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        nearestLocationRecyclerView.adapter = adapter
    }

    // Setup for Choose Location RecyclerView
    private fun setupChooseLocationRecyclerView() {
        // Dummy data for Choose Location
        val locations = listOf(
            Place("Central Java", "Yogyakarta, Indonesia", 0.0, R.drawable.central_java, "Explore cultural heritage in Central Java."),
            Place("Yogyakarta", "Yogyakarta, Indonesia", 0.0, R.drawable.yogyakarta, "Discover the beauty of Yogyakarta.")
        )

        // Setup RecyclerView
        val adapter = ChooseLocationAdapter(locations)
        chooseLocationRecyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        chooseLocationRecyclerView.adapter = adapter
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomePageFragment().apply {
            }
    }
}
