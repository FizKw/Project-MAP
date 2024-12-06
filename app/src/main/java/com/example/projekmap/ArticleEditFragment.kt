package com.example.projekmap

import android.Manifest.permission.READ_MEDIA_IMAGES
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ArticleEditFragment : Fragment() {

    private lateinit var db: FirebaseFirestore
    private lateinit var storage: FirebaseStorage

    private lateinit var articleImage: ImageView
    private lateinit var imageUri: Uri
    private var imageSelected = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_article_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val articleId = arguments?.getString("articleId")
        articleImage = view.findViewById<ImageView>(R.id.articleImagePreview)
        val editImageButton = view.findViewById<Button>(R.id.btnEditImage)
        val titleField = view.findViewById<EditText>(R.id.etTitle)
        val descField = view.findViewById<EditText>(R.id.etDescription)
        val saveButton = view.findViewById<Button>(R.id.btnSave)

        view.findViewById<TextView>(R.id.placeholder).text = articleId

        db = FirebaseFirestore.getInstance()
        storage = FirebaseStorage.getInstance()

        if (articleId != null) {
            db.collection("article").document(articleId).get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        val title = document.getString("title")
                        val desc = document.getString("description")
                        val imageUrl = document.getString("imageUrl")

                        titleField.setText(title)
                        descField.setText(desc)
                        Glide.with(requireContext()).load(imageUrl).into(articleImage)
                    }
                }.addOnFailureListener {
                    Toast.makeText(requireContext(), "Error fetching data", Toast.LENGTH_SHORT).show()
                }
        }

        saveButton.setOnClickListener {
            val title = titleField.text.toString()
            val desc = descField.text.toString()

            if (articleId != null) {
                val articleData = HashMap<String, Any>()
                articleData["title"] = title
                articleData["description"] = desc


                if (imageSelected){
                    val storageRef = storage.reference.child("article_images/${articleId}.jpg")
                    storageRef.putFile(imageUri)
                        .addOnSuccessListener {
                            storageRef.downloadUrl.addOnSuccessListener { uri ->
                                articleData["imageUrl"] = uri.toString()
                                db.collection("article").document(articleId).set(articleData).addOnSuccessListener {
                                    Toast.makeText(requireContext(), "Data updated", Toast.LENGTH_SHORT).show()
                                    findNavController().navigate(R.id.admin_page_fragment)
                                }.addOnFailureListener {
                                    Toast.makeText(requireContext(), "Error updating data", Toast.LENGTH_SHORT).show()
                                }
                            }
                        }
                } else {
                    db.collection("onboarding").document(articleId).set(articleData).addOnSuccessListener {
                        Toast.makeText(requireContext(), "Data updated", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.admin_page_fragment)
                    }.addOnFailureListener {
                        Toast.makeText(requireContext(), "Error updating data", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        editImageButton.setOnClickListener {
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


        view.findViewById<View>(R.id.back_button).setOnClickListener {
            findNavController().navigateUp()
        }






    }
    private val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()){
            uri ->
        if (uri != null){
            imageUri = uri
            articleImage.setImageURI(imageUri)
            imageSelected = true
            Toast.makeText(requireContext(), "Image Selected", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(requireContext(), "No Image Selected", Toast.LENGTH_SHORT).show()
        }
    }
}
