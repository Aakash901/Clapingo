package com.example.claptask.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.claptask.R

class SelectedItemAdapter(
    private var selectedItems: List<String>
) : RecyclerView.Adapter<SelectedItemAdapter.SelectedItemViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectedItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_selected_feedback, parent, false)
        return SelectedItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: SelectedItemViewHolder, position: Int) {
        holder.bind(selectedItems[position])
        holder.deleteIv.setOnClickListener {
            selectedItems = selectedItems.toMutableList().apply { removeAt(position) }
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return selectedItems.size
    }

    fun updateSelectedItems(newSelectedItems: List<String>) {
        selectedItems = newSelectedItems
        notifyDataSetChanged()
    }


    class SelectedItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val selectedItemTextView: TextView = itemView.findViewById(R.id.textViewSelectedItemHeading)
         val deleteIv: ImageView = itemView.findViewById(R.id.deleteIV)

        fun bind(selectedItem: String) {
            //show the name of selected aspect from category
            selectedItemTextView.text = selectedItem


        }

    }
}
