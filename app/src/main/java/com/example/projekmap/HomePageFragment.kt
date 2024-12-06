package com.example.projekmap

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.tasks.Task
import com.google.android.gms.tasks.Tasks
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import java.io.IOException
import java.util.ArrayList
import java.util.Locale
import java.util.UUID

class HomePageFragment : BaseAuthFragment() {

    private lateinit var popularNearbyRecyclerView: RecyclerView
    private lateinit var recommendedRecyclerView: RecyclerView
    private lateinit var nearestLocationRecyclerView: RecyclerView
    private lateinit var chooseLocationRecyclerView: RecyclerView
    private lateinit var articleRecyclerView: RecyclerView
    private lateinit var searchRecycleView: RecyclerView
    private lateinit var searchView: SearchView
    private lateinit var requestPermissionlauncher: ActivityResultLauncher<String>
    private lateinit var mMap: GoogleMap
//    private lateinit var geocoder: Geocoder
    private lateinit var locationText: TextView
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient

    private lateinit var db : FirebaseFirestore


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


        // Initialize RecyclerViews
        popularNearbyRecyclerView = view.findViewById(R.id.popularNearbyRecyclerView)
        recommendedRecyclerView = view.findViewById(R.id.recommendedRecyclerView)
        articleRecyclerView = view.findViewById(R.id.articleRecyclerView) // Initialize article RecyclerView
        searchView = view.findViewById<SearchView>(R.id.search_here)
        searchRecycleView = view.findViewById<RecyclerView>(R.id.searchRecyclerView)

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

                    // Setup RecyclerView
                    setupSearchView()
                    setupPopularNearbyRecyclerView(latLng)
                    setupRecommendedRecyclerView()
                    setupArticleRecyclerView()// Add setup for Choose Location
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

    private fun setupSearchView() {
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (!query.isNullOrEmpty()) {
                    setupSearchRecyclerView(query)
                }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // Clear the RecyclerView and hide it if the query is empty
                    searchRecycleView.adapter = null
                    searchRecycleView.visibility = View.GONE
                }
                return true
            }
        })
    }



    // Setup for Popular Nearby RecyclerView
    private fun setupSearchRecyclerView(searchQuery: String) {
        db = FirebaseFirestore.getInstance()

        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()

        // Firestore query with SearchView input
        val vendorQuery = db.collection("vendors")
            .orderBy("vendor") // Ensure "vendor" field is indexed in Firestore
            .startAt(searchQuery)
            .endAt(searchQuery + "\uf8ff")
            .limit(3)
        tasks.add(vendorQuery.get())

        // Query for matching place names
        val placeQuery = db.collection("vendors")
            .orderBy("place") // Ensure "place" field is indexed in Firestore
            .startAt(searchQuery)
            .endAt(searchQuery + "\uf8ff")
            .limit(3)
        tasks.add(placeQuery.get())

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.e("Error", "Error getting documents: ", task.exception)
                    return@addOnCompleteListener
                }

                val matchingDocs: MutableList<Place> = ArrayList()
                for (t in tasks) {
                    val snap = t.result
                    if (snap != null) {
                        for (doc in snap.documents) {
                            val place = Place(
                                id = doc.id,
                                vendor = doc.getString("vendor") ?: "Unknown Vendor",
                                place = doc.getString("place") ?: "Unknown Place",
                                avgRating = doc.getDouble("avg_rating") ?: 0.0,
                                vendorImage = doc.getString("vendor_image") ?: "",
                                desc = doc.getString("desc") ?: "Unknown Description",
                                estimate = doc.getString("estimate") ?: "Unknown Estimate",
                                type = doc.getString("type") ?: "Unknown Type",
                                via = doc.getString("via") ?: "Unknown Via",
                            )
                            matchingDocs.add(place)
                        }
                    }
                }

                // Deduplicate and sort results if needed
                val sortedDocs = matchingDocs.distinctBy { it.id }.sortedByDescending { it.avgRating }.take(5)

                // Update the RecyclerView
                val adapter = PopularNearbyAdapter(sortedDocs)
                searchRecycleView.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                searchRecycleView.adapter = adapter

                // Show RecyclerView if results are found
                searchRecycleView.visibility = if (sortedDocs.isNotEmpty()) View.VISIBLE else View.GONE
            }
            .addOnFailureListener {
                Log.e("Error", "Error getting documents: ", it)
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun setupPopularNearbyRecyclerView(latLng: LatLng) {
        db = FirebaseFirestore.getInstance()

        val center = GeoLocation(latLng.latitude, latLng.longitude)
        val radiusInM = 50.0 * 1000.0

        val bounds = GeoFireUtils.getGeoHashQueryBounds(center, radiusInM)
        val tasks: MutableList<Task<QuerySnapshot>> = ArrayList()

        for (b in bounds) {
            val q = db.collection("vendors")
                .orderBy("geohash")
                .startAt(b.startHash)
                .endAt(b.endHash)
                .limit(5)
            tasks.add(q.get())
        }

        Tasks.whenAllComplete(tasks)
            .addOnCompleteListener{ task ->
                if (!task.isSuccessful){
                    Log.e("Error", "Error getting documents: ", task.exception)
                    return@addOnCompleteListener
                }

                val matchingDocs: MutableList<Place> = ArrayList()
                for (t in tasks){
                    val snap = t.result
                    if (snap != null){
                        for (doc in snap.documents){
                            val lat = doc.getDouble("lat")!!
                            val lng = doc.getDouble("long")!!

                            val docLocation = GeoLocation(lat, lng)
                            val distanceInM = GeoFireUtils.getDistanceBetween(docLocation, center)
                            if (distanceInM <= radiusInM){
                                val place = Place(
                                    id = doc.id,
                                    vendor = doc.getString("vendor")?: "Unknown Vendor",
                                    place = doc.getString("place")?: "Unknown Place",
                                    avgRating = doc.getDouble("avg_rating")?: 0.0,
                                    vendorImage = doc.getString("vendor_image")?: "",
                                    desc = doc.getString("desc")?: "Unknwon Description",
                                    estimate = doc.getString("estimate")?: "Unknown Estimate",
                                    type = doc.getString("type")?: "Unknown Type",
                                    via = doc.getString("via")?: "Unknown Via",
                                )
                                matchingDocs.add(place)
//                                Toast.makeText(requireContext(), "Success ${doc.getString("vendor")}", Toast.LENGTH_SHORT).show()
                            }

                        }
                    }
                }
                val uniqueDocs = matchingDocs.distinctBy { it.id }.sortedByDescending { it.avgRating }.take(3)


                val adapter = PopularNearbyAdapter(uniqueDocs)
                popularNearbyRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                popularNearbyRecyclerView.adapter = adapter
            }
            .addOnFailureListener {
                Log.e("Error", "Error getting documents: ", it)
                Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
            }


    }



    private fun setupRecommendedRecyclerView() {
        val recommendedPlaces = listOf(
            Place(UUID.randomUUID().toString(), "East Java, Indonesia", "loc 1", 4.5, "Semeru Mountain is the highest volcano in Java.", "desc", "est 1", "type 1", "via 1"),
            Place(UUID.randomUUID().toString(), "West Papua, Indonesia", "loc 2", 4.7, "Raja Ampat is famous for its stunning marine biodiversity.", "desc2", "est 2", "type 2", "via 2"),
            Place(UUID.randomUUID().toString(), "Bali, Indonesia", "loc 3", 4.7, "Bali is a popular tourist destination known for its beaches and temples.", "desc 3", "est 3", "type 3", "via 3")
        )
        val adapter = RecommendedAdapter(recommendedPlaces)
        recommendedRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        recommendedRecyclerView.adapter = adapter
    }

    // Setup for Article RecyclerView
    private fun setupArticleRecyclerView() {

        db = FirebaseFirestore.getInstance()
        val articleCollection = db.collection("article")
        val articles = mutableListOf<Article>()

        val documentIds = listOf("article1", "article2", "article3")
        documentIds.forEach { documentId ->
            articleCollection.document(documentId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val title = document.getString("title")
                        val description = document.getString("description")
                        val imageRes = document.getString("imageUrl")
                        if (title != null && description != null && imageRes != null) {
                            articles.add(Article(title, description, imageRes))

                            if (articles.size == documentIds.size) {
                                // Initialize RecyclerView
                                val adapter = ArticleAdapter(articles)
                                articleRecyclerView.layoutManager =
                                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                                articleRecyclerView.adapter = adapter
                            }
                        }
                    }
                }
                .addOnFailureListener {
                    Log.e("Error", "Error getting documents: ", it)
                    Toast.makeText(requireContext(), "Error: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        }

//        val articles = listOf(
//            Article("Discover Yogyakarta", "lorep ipsum", "Jan 2023" , R.drawable.yogyakarta),
//            Article("Cultural Gems of Central Java", "lorep ipsum", "Feb 2023", R.drawable.central_java)
//        )

//        val adapter = ArticleAdapter(articles)
//        articleRecyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        articleRecyclerView.adapter = adapter
    }

}
