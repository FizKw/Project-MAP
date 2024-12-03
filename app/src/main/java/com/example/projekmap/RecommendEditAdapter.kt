package com.example.projekmap

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class RecommendEditAdapter(private val items: List<Recommend>) : RecyclerView.Adapter<RecommendEditAdapter.ViewHolder>() {

    // Class ViewHolder
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val checkboxButton: ImageView = itemView.findViewById(R.id.checkbox_button)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recommend_edit, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        var isChecked = false

        // Set initial state of checkbox
        holder.checkboxButton.setImageResource(R.drawable.ic_checkbox)

        // Set OnClickListener
        holder.checkboxButton.setOnClickListener {
            isChecked = !isChecked
            val drawable = if (isChecked) R.drawable.ic_checkboxfull else R.drawable.ic_checkbox
            holder.checkboxButton.setImageResource(drawable)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }
}
