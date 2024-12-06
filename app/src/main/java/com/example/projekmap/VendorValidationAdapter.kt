package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class VendorValidationAdapter(private val validationList: List<VendorValidation>) : RecyclerView.Adapter<VendorValidationAdapter.ValidationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ValidationViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_validasi_vendor, parent, false)
        return ValidationViewHolder(view)
    }

    override fun onBindViewHolder(holder: ValidationViewHolder, position: Int) {
        val vendorValidation = validationList[position]
        holder.bind(vendorValidation)
    }

    override fun getItemCount(): Int = validationList.size

    class ValidationViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val terimaButton: View = itemView.findViewById(R.id.terima_button)
        private val tolakButton: View = itemView.findViewById(R.id.tolak_button)
        private val vendorName: TextView = itemView.findViewById(R.id.menu_title)

        fun bind(vendorValidation: VendorValidation) {
            vendorName.text = vendorValidation.vendor

            val db = FirebaseFirestore.getInstance()
            val userRef = db.collection("users").document(vendorValidation.uid)
            // Tampilkan Toast ketika tombol "Terima" diklik
            terimaButton.setOnClickListener {
                userRef.update("status", true)
                    .addOnSuccessListener {
                        Toast.makeText(
                            itemView.context,
                            "Vendor diterima!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            itemView.context,
                            "Gagal menerima vendor!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
            }

            // Tampilkan Toast ketika tombol "Tolak" diklik
            tolakButton.setOnClickListener {
                userRef.update("status", false)
                    .addOnSuccessListener {
                        Toast.makeText(
                            itemView.context,
                            "Vendor ditolak!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }.addOnFailureListener {
                        Toast.makeText(
                            itemView.context,
                            "Gagal menolak vendor!",
                            Toast.LENGTH_SHORT
                        ).show()
                    }


            }
        }
    }
}
