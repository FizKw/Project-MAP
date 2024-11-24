package com.example.projekmap

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.Manifest.permission.READ_MEDIA_IMAGES
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.imageview.ShapeableImageView
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import java.util.UUID


class UpdateProfileFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var profileImage: ShapeableImageView
    private lateinit var imageUri: Uri
    private var imageSelected = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_profile, container, false)
    }


    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
        uri ->
        if (uri != null){
            imageUri = uri
            profileImage.setImageURI(imageUri)
            imageSelected = true
            Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }

    fun saveUserData(userData: HashMap<String, String>){
        val user = auth.currentUser
        if (user != null){
            db.collection("users").document(user.uid).set(userData, SetOptions.merge())
                .addOnSuccessListener {
                    Toast.makeText(requireContext(), "Profile Updated", Toast.LENGTH_SHORT).show()
                    findNavController().navigate(R.id.profileFragment)
                }
                .addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error updating information: ${e.message}", Toast.LENGTH_SHORT).show()
                }
        }
    }





    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameInputField = view.findViewById<TextInputEditText>(R.id.name_input_field)
        val phoneNumberInputField = view.findViewById<TextInputEditText>(R.id.phone_number_input_field)
        val saveButton = view.findViewById<Button>(R.id.save_button)
        profileImage = view.findViewById<ShapeableImageView>(R.id.profile_image)
        val uploadImageButton = view.findViewById<Button>(R.id.upload_image_button)

        auth = FirebaseAuth.getInstance()
        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        val user = auth.currentUser
        if (user != null){
            db.collection("users").document(user.uid).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val name = document.getString("name")
                        val phoneNumber = document.getString("phone_number")
                        val profilePicture = document.getString("profile_picture")

                        nameInputField.setText(name)
                        phoneNumberInputField.setText(phoneNumber)
                        if (!profilePicture.isNullOrEmpty()) {
                            Glide.with(this)
                                .load(profilePicture)
                                .into(profileImage)
                        }
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
                }

            saveButton.setOnClickListener{
                val name = nameInputField.text.toString()
                val phoneNumber = phoneNumberInputField.text.toString()

                if(name.isNotEmpty() && phoneNumber.isNotEmpty()){
                    val userData = HashMap<String, String>()
                    userData["name"] = name
                    userData["phone_number"] = phoneNumber
                    if (imageSelected){
                        val storageRef = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")
                        storageRef.putFile(imageUri)
                            .addOnSuccessListener {
                                storageRef.downloadUrl.addOnSuccessListener { uri ->
                                    userData["profile_picture"] = uri.toString()
                                    saveUserData(userData)
                                }
                            }

                    }else {
                        saveUserData(userData)
                    }



                } else {
                    Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
                }
            }
        }

        uploadImageButton.setOnClickListener{
            if (
                Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU &&
                ContextCompat.checkSelfPermission(requireContext(), READ_MEDIA_IMAGES) != PERMISSION_GRANTED
                )
            {
                ActivityCompat.requestPermissions(requireActivity(), arrayOf(READ_MEDIA_IMAGES), 1001)
            } else {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }
        }
    }


}