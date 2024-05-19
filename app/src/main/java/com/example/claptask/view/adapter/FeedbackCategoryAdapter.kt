package com.example.claptask.view.adapter

import android.animation.ObjectAnimator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.claptask.R
import com.example.claptask.model.FeedbackCategory
import com.example.claptask.view.HomeActivity

class FeedbackCategoryAdapter(
    var categories: List<FeedbackCategory>,
    private val drawables: List<Int>,
    val parentContext: HomeActivity,
    private val submitButton: Button

) :
    RecyclerView.Adapter<FeedbackCategoryAdapter.ViewHolder>() {

    private var expandedPosition: Int = RecyclerView.NO_POSITION

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_feedback_category, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val category = categories[position]
        val drawableResId = drawables.getOrNull(position) ?: R.drawable.others
        holder.bind(category, drawableResId, position)

    }

    override fun getItemCount(): Int {
        return categories.size
    }

    //this will return  weather all category are selected or not

    fun areAllCategoriesSelected(): Boolean {
        for (category in categories) {
            val viewHolder = parentContext.binding.recyclerViewFeedback.findViewHolderForAdapterPosition(categories.indexOf(category)) as? ViewHolder
            val selectedCount = viewHolder?.getSelectedCount() ?: 0
            if (selectedCount == 0) {
                return false
            }
        }
        return true
    }

    //dynamically handling the color of submit button

    private fun updateSubmitButtonColor() {
        if (areAllCategoriesSelected()) {
            submitButton.setBackgroundColor(ContextCompat.getColor(parentContext, R.color.green))

        } else {
            submitButton.setBackgroundColor(ContextCompat.getColor(parentContext,
                R.color.light_green
            ))

        }
    }


    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val categoryNameTextView: TextView =
            itemView.findViewById(R.id.textViewCategoryName)
        private val logo: ImageView = itemView.findViewById(R.id.imageLogo)
        private val arrowImage: ImageView = itemView.findViewById(R.id.arrow)
        private val feedbackItemsRecyclerView: RecyclerView =
            itemView.findViewById(R.id.recyclerViewFeedbackItems)
        private val selectedCountTextView: TextView = itemView.findViewById(R.id.circularTextView)
        private val llCount: LinearLayout = itemView.findViewById(R.id.textViewSelectedCount)
        private val selectedItemsRecyclerView: RecyclerView = itemView.findViewById(R.id.recyclerViewSelectedItems)
        private val selectedItemAdapter = SelectedItemAdapter(emptyList())

        fun bind(category: FeedbackCategory, drawableResId: Int, position: Int) {
            categoryNameTextView.text = category.categoryName
            logo.setImageResource(drawableResId)


            feedbackItemsRecyclerView.layoutManager = GridLayoutManager(itemView.context, 2)
            feedbackItemsRecyclerView.adapter = FeedbackItemAdapter(
                category.feedbackItems,
                parentContext,
                category.categoryName,
                { selectedCount ->
                    if (selectedCount > 0) {
                        llCount.visibility = View.VISIBLE
                        selectedCountTextView.text = " $selectedCount"
                    } else {
                        llCount.visibility = View.GONE
                    }
                    updateSubmitButtonColor()
                },
                { selectedItems ->
                    selectedItemAdapter.updateSelectedItems(selectedItems)
                }
            )

            selectedItemsRecyclerView.layoutManager = LinearLayoutManager(itemView.context)
            selectedItemsRecyclerView.adapter = selectedItemAdapter


            val isExpanded = position == expandedPosition

            feedbackItemsRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE
            selectedItemsRecyclerView.visibility = if (isExpanded) View.VISIBLE else View.GONE

            if (isExpanded) {
                rotateImage(arrowImage, 90f)
            } else {
                rotateImage(arrowImage, 0f)
            }
            itemView.setOnClickListener {

                expandedPosition = if (isExpanded) RecyclerView.NO_POSITION else position
                notifyDataSetChanged()
            }

        }

        fun getSelectedCount(): Int {
            return selectedCountTextView.text.toString().trim().toIntOrNull() ?: 0
        }

        //to rotate the arrow image by 90 degree
        private fun rotateImage(view: ImageView, degree: Float) {
            val animator = ObjectAnimator.ofFloat(view, "rotation", degree)
            animator.duration = 100
            animator.start()
        }
    }
}
