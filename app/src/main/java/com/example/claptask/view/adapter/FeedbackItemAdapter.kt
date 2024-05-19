package com.example.claptask.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.claptask.view.HomeActivity
import com.example.claptask.R
import com.example.claptask.model.FeedbackItem
import de.hdodenhof.circleimageview.CircleImageView

class FeedbackItemAdapter(
    private val feedbackItems: List<FeedbackItem>,
    val parentContext: HomeActivity,
    val categoryName: String,
    val onSelectionChanged: (Int) -> Unit,
    val onSelectedItemsUpdated: (List<String>) -> Unit
) :
    RecyclerView.Adapter<FeedbackItemAdapter.FeedbackItemViewHolder>() {

    private val selectedItems = mutableSetOf<Int>()
    private val selectedItemsFromBottomSheet = mutableSetOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedbackItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feedback, parent, false)
        return FeedbackItemViewHolder(view)
    }

    override fun onBindViewHolder(holder: FeedbackItemViewHolder, position: Int) {
        val feedbackItem = feedbackItems[position]
        holder.bind(feedbackItem)
    }

    override fun getItemCount(): Int {
        return feedbackItems.size
    }

    inner class FeedbackItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val aspectTextView: TextView = itemView.findViewById(R.id.textViewAspect)
        private val positiveImage: CircleImageView = itemView.findViewById(R.id.positiveImage)
        private val negativeImage: CircleImageView = itemView.findViewById(R.id.negativeImage)

        fun bind(feedbackItem: FeedbackItem) {
            aspectTextView.text = feedbackItem.aspect

            // Set click listeners for did well section
            positiveImage.setOnClickListener {
                positiveImage.borderColor = ContextCompat.getColor(itemView.context, R.color.green)
                negativeImage.borderColor = ContextCompat.getColor(itemView.context, R.color.gray)
                selectedItems.add(position)
                onSelectionChanged(selectedItems.size)
                if (!feedbackItem.didWell.size.equals(1)) {
                    parentContext.showBottomSheet(feedbackItem.didWell, categoryName, ::onBottomSheetSelection)
                }
                updateSelectedItemsList()
            }
            // Set click listeners for scope of improvement  section
            negativeImage.setOnClickListener {
                negativeImage.borderColor = ContextCompat.getColor(itemView.context, R.color.red)
                positiveImage.borderColor = ContextCompat.getColor(itemView.context, R.color.gray)
                selectedItems.add(position)
                onSelectionChanged(selectedItems.size)
                if (!feedbackItem.scopeOfImprovement.size.equals(1)) {
                    parentContext.showBottomSheet(feedbackItem.scopeOfImprovement, categoryName, ::onBottomSheetSelection)
                }
                updateSelectedItemsList()
            }
        }

        //it will show bottom sheet of there is more then 1 item are present in the list
        private fun onBottomSheetSelection(selectedItems: List<String>) {
            selectedItemsFromBottomSheet.clear()
            selectedItemsFromBottomSheet.addAll(selectedItems)
            onSelectionChanged(this@FeedbackItemAdapter.selectedItems.size + selectedItemsFromBottomSheet.size)
        }


        private fun updateSelectedItemsList() {
            val allSelectedItems = feedbackItems.filterIndexed { index, _ -> selectedItems.contains(index) }
                .map { it.aspect } + selectedItemsFromBottomSheet
            onSelectedItemsUpdated(allSelectedItems)
        }
    }


}
