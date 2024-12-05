package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class VendorListAdapter(private val vendorList: List<Vendor>) : RecyclerView.Adapter<VendorListAdapter.VendorViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VendorViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vendor_list, parent, false)
        return VendorViewHolder(view)
    }

    override fun onBindViewHolder(holder: VendorViewHolder, position: Int) {
        val vendor = vendorList[position]
        holder.bind(vendor)
    }

    override fun getItemCount(): Int = vendorList.size

    class VendorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val vendorName: TextView = itemView.findViewById(R.id.menu_title)
        private val editButton: Button = itemView.findViewById(R.id.edit_button)

        fun bind(vendor: Vendor) {
            vendorName.text = vendor.name
            // Optionally, add a listener for the edit button
            editButton.setOnClickListener {
                // Handle edit action here (for example, open another screen or dialog)
            }
        }
    }
}

