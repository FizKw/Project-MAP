package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

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

        fun bind(vendorValidation: VendorValidation) {
            // Tampilkan Toast ketika tombol "Terima" diklik
            terimaButton.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Vendor diterima!",
                    Toast.LENGTH_SHORT
                ).show()
            }

            // Tampilkan Toast ketika tombol "Tolak" diklik
            tolakButton.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Vendor ditolak!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}
