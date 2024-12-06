package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView

class RecommendEditAdapter(private val items: List<Recommend>, private val selectedItems: MutableList<String>) : RecyclerView.Adapter<RecommendEditAdapter.ViewHolder>() {

    // Class ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vendorName: TextView = itemView.findViewById(R.id.vendor_name)
        val vendorPlace: TextView = itemView.findViewById(R.id.vendor_place)
        val checkboxButton: ImageView = itemView.findViewById(R.id.checkbox_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        var isChecked = selectedItems.contains(item.id)

        holder.vendorName.text = item.vendor
        holder.vendorPlace.text = item.place
        holder.checkboxButton.setImageResource(if(isChecked) R.drawable.ic_checkboxfull else R.drawable.ic_checkbox)

        // Set OnClickListener
        holder.checkboxButton.setOnClickListener {
            if (isChecked) {
                // Deselect item
                selectedItems.remove(item.id)
            } else {
                // Limit selection to 3
                if (selectedItems.size < 3) {
                    selectedItems.add(item.id)
                } else {
                    Toast.makeText(holder.itemView.context, "You can only select 3 items.", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            // Toggle checkbox state
            isChecked = !isChecked
            holder.checkboxButton.setImageResource(if (isChecked) R.drawable.ic_checkboxfull else R.drawable.ic_checkbox)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
