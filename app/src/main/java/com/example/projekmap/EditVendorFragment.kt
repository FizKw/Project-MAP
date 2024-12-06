package com.example.projekmap

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.firebase.geofire.GeoFireUtils
import com.firebase.geofire.GeoLocation
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.GeoPoint
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.io.IOException
import java.util.UUID


class EditVendorFragment : Fragment(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var geocoder: Geocoder
    private lateinit var locationText: TextView
    private lateinit var mapSearchView: SearchView
    private lateinit var storage: FirebaseStorage
    private var latLng: LatLng? = null
    private var geoHash: String? = null
    private var latlngselected = false

    private lateinit var vendorImage: ShapeableImageView
    private lateinit var imageUri: Uri
    private var imageSelected = false
    private var imageAvailable = false

    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            uri ->
        if (uri != null){
            imageUri = uri
            vendorImage.setImageURI(imageUri)
            imageSelected = true
            imageAvailable = true
            Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_edit_vendor, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        storage = FirebaseStorage.getInstance()

        vendorImage = view.findViewById<ShapeableImageView>(R.id.vendor_image)
        val vendorInputField = view.findViewById<TextInputEditText>(R.id.vendor_input_field)
        val placeInputField = view.findViewById<TextInputEditText>(R.id.place_input_field)
        val typeInputField = view.findViewById<TextInputEditText>(R.id.type_input_field)
        val estimateInputField = view.findViewById<TextInputEditText>(R.id.estimate_input_field)
        val viaInputField = view.findViewById<TextInputEditText>(R.id.via_input_field)
        val descinputField = view.findViewById<TextInputEditText>(R.id.desc_input_field)
//
        val searchMapButton = view.findViewById<Button>(R.id.search_map_button)
//
        val saveButton = view.findViewById<Button>(R.id.save_button)
        val latView = view.findViewById<TextView>(R.id.latitude_view)
        val longView = view.findViewById<TextView>(R.id.longitude_view)
        val db = FirebaseFirestore.getInstance()

        vendorImage.setOnClickListener{
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) != PERMISSION_GRANTED
            )
            {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_MEDIA_IMAGES), 1011)
            } else {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }



        val userId = arguments?.getString("uid")

        if (userId != null) {
            db.collection("vendors").document(userId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val vendor = document.getString("vendor")
                        val vendorPicture = document.getString("vendor_image")
                        val place = document.getString("place")
                        val type = document.getString("type")
                        val estimate = document.getString("estimate")
                        val via = document.getString("via")
                        val desc = document.getString("desc")
                        val lat = document.getGeoPoint("lat_long")!!.latitude.toString()
                        val long = document.getGeoPoint("lat_long")!!.longitude.toString()
                        geoHash = document.getString("geohash")


                        vendorInputField.setText(vendor)
                        placeInputField.setText(place)
                        typeInputField.setText(type)
                        estimateInputField.setText(estimate)
                        viaInputField.setText(via)
                        descinputField.setText(desc)
                        latView.text = lat
                        longView.text = long

                        Glide.with(this)
                            .load(vendorPicture)
                            .into(vendorImage)

                        latLng = LatLng(document.getGeoPoint("lat_long")!!.latitude, document.getGeoPoint("lat_long")!!.longitude)
                        latlngselected = true
                        imageAvailable = true
                    }
                    else{
                        Toast.makeText(requireContext(), "No data found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(requireContext(), "Failed to get user Data", Toast.LENGTH_SHORT).show()
                }
        }





        mapSearchView = view.findViewById(R.id.map_search_view)

        val selectLocationButton = view.findViewById<Button>(R.id.select_button)

        selectLocationButton.visibility = View.GONE
        mapSearchView.visibility = View.GONE



        val mapFragment = childFragmentManager.findFragmentById(R.id.map_view) as SupportMapFragment
        childFragmentManager.beginTransaction().hide(mapFragment).commit()



//  ========================================== JANGAN DIHAPUS =========================================================      
//        saveButton.setOnClickListener{
//            val vendor = vendorInputField.text.toString().trim()
//            val place = placeInputField.text.toString().trim()
//            val type = typeInputField.text.toString().trim()
//            val estimate = estimateInputField.text.toString().trim()
//            val via = viaInputField.text.toString().trim()
//            val desc = descinputField.text.toString().trim()
//            val lat: Double = latLng!!.latitude
//            val long: Double = latLng!!.longitude
//
//            if(userId.isNullOrEmpty()||vendor.isEmpty()||place.isEmpty()||type.isEmpty()||estimate.isEmpty()||via.isEmpty()||desc.isEmpty() || !latlngselected ||!imageAvailable){
//                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show()
//            }else{
////                GeoPoint(latLng!!.latitude, latLng!!.longitude)\
//                val storageRef = storage.reference.child("vendor_images/${UUID.randomUUID()}.jpg")
//                geoHash = GeoFireUtils.getGeoHashForLocation(GeoLocation(latLng!!.latitude, latLng!!.longitude))
//
//                val vendorData = HashMap<String, Any>()
//                vendorData["vendor"] = vendor
//                vendorData["place"] = place
//                vendorData["type"] = type
//                vendorData["estimate"] = estimate
//                vendorData["via"] = via
//                vendorData["desc"] = desc
//                vendorData["lat"] = lat
//                vendorData["long"] = long
//                vendorData["lat_long"] = GeoPoint(latLng!!.latitude, latLng!!.longitude)
//                vendorData["geohash"] = geoHash!!
//
//                if (imageAvailable){
//                    if (imageSelected){
//                        storageRef.putFile(imageUri)
//                            .addOnSuccessListener {
//                                storageRef.downloadUrl.addOnSuccessListener { uri ->
//                                    vendorData["vendor_image"] = uri.toString()
//                                    db.collection("vendors").document(userId).set(vendorData)
//                                        .addOnSuccessListener {
//                                            Toast.makeText(requireContext(), "Data saved", Toast.LENGTH_SHORT).show()
//                                            findNavController().navigate(R.id.profileFragment)
//                                        }
//                                        .addOnFailureListener {
//                                            Toast.makeText(requireContext(), "Data not saved", Toast.LENGTH_SHORT).show()
//                                        }
//                                }
//                            }
//                    }
//                    else {
//                        db.collection("vendors").document(userId).set(vendorData)
//                            .addOnSuccessListener {
//                                Toast.makeText(requireContext(), "Data saved", Toast.LENGTH_SHORT).show()
//                                findNavController().navigate(R.id.profileFragment)
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(requireContext(), "Data not saved", Toast.LENGTH_SHORT).show()
//                            }
//                    }
//                }
//            }
//        }

//  ========================================== JANGAN DIHAPUS =========================================================
//        searchMapButton.setOnClickListener {
//            vendorInputField.visibility = View.GONE
//            placeInputField.visibility = View.GONE
//            typeInputField.visibility = View.GONE
//            estimateInputField.visibility = View.GONE
//            viaInputField.visibility = View.GONE
//            descinputField.visibility = View.GONE
//            searchMapButton.visibility = View.GONE
//            latView.visibility = View.GONE
//            longView.visibility = View.GONE
//            saveButton.visibility = View.GONE
//            vendorImage.visibility = View.GONE
//
//
//            mapSearchView.visibility = View.VISIBLE
//            childFragmentManager.beginTransaction().show(mapFragment).commit()
//
//
//            mapSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
//                override fun onQueryTextSubmit(query: String?): Boolean {
//                    val location: String = mapSearchView.query.toString()
//                    var addressList: List<Address> = emptyList()
//                    mMap.clear()
//
//                    if(location.isNotEmpty()) {
//                        geocoder = Geocoder(requireContext())
//                        try {
//                            addressList = geocoder.getFromLocationName(location, 1)!!
//                        } catch (e: IOException) {
//                            e.printStackTrace()
//                        }
//                        val address: Address = addressList[0]
//                        latLng = LatLng(address.latitude, address.longitude)
//                        mMap.addMarker(MarkerOptions().position(latLng!!).title(location))
//                        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng!!, 10.0F))
//                        selectLocationButton.visibility = View.VISIBLE
//                        selectLocationButton.setOnClickListener{
//                            latView.text = address.latitude.toString()
//                            longView.text = address.longitude.toString()
//                            vendorInputField.visibility = View.VISIBLE
//                            placeInputField.visibility = View.VISIBLE
//                            typeInputField.visibility = View.VISIBLE
//                            estimateInputField.visibility = View.VISIBLE
//                            viaInputField.visibility = View.VISIBLE
//                            descinputField.visibility = View.VISIBLE
//                            searchMapButton.visibility = View.VISIBLE
//                            latView.visibility = View.VISIBLE
//                            longView.visibility = View.VISIBLE
//                            saveButton.visibility = View.VISIBLE
//                            vendorImage.visibility = View.VISIBLE
//
//                            mapSearchView.visibility = View.GONE
//                            selectLocationButton.visibility = View.GONE
//                            childFragmentManager.beginTransaction().hide(mapFragment).commit()
//                            latlngselected = true
//                        }
//                    }
//                    else{
//                        Toast.makeText(requireContext(), "Location not found", Toast.LENGTH_SHORT).show()
//                    }
//
//                    return false
//                }
//
//                override fun onQueryTextChange(newText: String?): Boolean {
//                    return false
//                }
//            })
//            mapFragment.getMapAsync(this)
//        }
//  ========================================== JANGAN DIHAPUS =========================================================
    }



    override fun onMapReady(p0: GoogleMap) {
        mMap = p0
    }


}