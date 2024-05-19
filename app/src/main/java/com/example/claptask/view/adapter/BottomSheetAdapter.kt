package com.example.claptask.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.claptask.R

class BottomSheetAdapter(
    private val items: List<String>,
    private val selectionCallback: (List<String>) -> Unit
) : RecyclerView.Adapter<BottomSheetAdapter.ItemViewHolder>() {

    private val selectedItems = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_sheet, parent, false)
        return ItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val textView: TextView = itemView.findViewById(R.id.textViewItem)

        fun bind(item: String) {
            textView.text = item

            // Set background color based on selection status
            if (selectedItems.contains(item)) {
                textView.setBackgroundColor(ContextCompat.getColor(itemView.context, R.color.green))
            } else {
                textView.setBackgroundColor(ContextCompat.getColor(itemView.context, android.R.color.transparent))
            }

            // Item click listener to toggle selection
            itemView.setOnClickListener {
                toggleSelection(item)
            }
        }

        private fun toggleSelection(item: String) {
            if (selectedItems.contains(item)) {
                selectedItems.remove(item)
            } else {
                selectedItems.add(item)
            }
            notifyItemChanged(adapterPosition)
            selectionCallback(selectedItems.toList())
        }
    }
}
