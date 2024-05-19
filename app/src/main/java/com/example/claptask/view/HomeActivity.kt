package com.example.claptask.view

import android.animation.ObjectAnimator
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.claptask.R
import com.example.claptask.databinding.ActivityHomeBinding
import com.example.claptask.model.ResponseData
import com.example.claptask.view.adapter.BottomSheetAdapter
import com.example.claptask.view.adapter.FeedbackCategoryAdapter
import com.example.claptask.viewmodel.FeedbackViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.button.MaterialButton

class HomeActivity : AppCompatActivity() {

    private val viewModel: FeedbackViewModel by viewModels()
    lateinit var binding: ActivityHomeBinding
    private lateinit var feedbackCategoryAdapter: FeedbackCategoryAdapter
    private lateinit var submitButton: Button

    private val drawablesList = listOf(
        R.drawable.confidence,
        R.drawable.grammar,
        R.drawable.fluency,
        R.drawable.pronunciation,

        )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        submitButton = findViewById(R.id.submitBtn)
        binding.progressBar.visibility = View.VISIBLE

        setupRecyclerView()
        setStatusBarColor()

        //observing the data and updating the rv
        viewModel.feedbackData.observe(this) { responseData ->
            responseData?.let {
                displayFeedbackData(it)
            }
        }

        viewModel.fetchFeedbackData()

        binding.otherBtn.setOnClickListener {
            if (binding.contentOther.visibility == View.VISIBLE) {
                rotateImage(binding.arrow2, 0f)
                binding.contentOther.visibility = View.GONE
            } else {
                binding.contentOther.visibility = View.VISIBLE
                rotateImage(binding.arrow2, 90f)
            }
        }

        //final button ,validating the data before final submit
        submitButton.setOnClickListener {
            if (feedbackCategoryAdapter.areAllCategoriesSelected()) {
                binding.btmHeader.text = resources.getText(R.string.btm_header)
                binding.btmHeader.setTextColor(ContextCompat.getColor(this, android.R.color.black))
                binding.scrollViewLayout.visibility = View.GONE
                binding.submitBtn.visibility = View.GONE
                binding.btmHeader.visibility = View.GONE
                binding.thankULayout.visibility = View.VISIBLE
            } else {
                binding.btmHeader.text = resources.getText(R.string.btm_header_error)
                binding.btmHeader.setTextColor(
                    ContextCompat.getColor(
                        this,
                        android.R.color.holo_red_dark
                    )
                )
                binding.scrollViewLayout.visibility = View.VISIBLE
                binding.submitBtn.visibility = View.VISIBLE
                binding.btmHeader.visibility = View.VISIBLE
                binding.thankULayout.visibility = View.GONE
            }
        }


    }

    private fun setStatusBarColor() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ContextCompat.getColor(this, R.color.light_green)
        }
    }

    private fun rotateImage(view: ImageView, degree: Float) {
        val animator = ObjectAnimator.ofFloat(view, "rotation", degree)
        animator.duration = 100
        animator.start()
    }

    private fun setupRecyclerView() {
        binding.recyclerViewFeedback.layoutManager = LinearLayoutManager(this)
        feedbackCategoryAdapter =
            FeedbackCategoryAdapter(emptyList(), drawablesList, this, submitButton)
        binding.recyclerViewFeedback.adapter = feedbackCategoryAdapter
    }

    private fun displayFeedbackData(responseData: ResponseData) {

        feedbackCategoryAdapter.categories = responseData.feedbackCategories
        feedbackCategoryAdapter.notifyDataSetChanged()
        binding.otherBtn.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE


    }

    // showing bottom sheet to the user if any category's child have more then  items

    public fun showBottomSheet(
        items: List<String>,
        categoryName: String,
        onSelectionChanged: (List<String>) -> Unit
    ) {
        val bottomSheetDialog = BottomSheetDialog(this)
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_layout, null)

        val headingTv = bottomSheetView.findViewById<TextView>(R.id.headingTV)
        val cancelIv = bottomSheetView.findViewById<ImageView>(R.id.cancelIv)
        val doneBtn = bottomSheetView.findViewById<MaterialButton>(R.id.doneBtn)
        headingTv.text = categoryName

        val recyclerView = bottomSheetView.findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        val adapter = BottomSheetAdapter(items) { selectedItems ->
            onSelectionChanged(selectedItems)
        }
        recyclerView.adapter = adapter
        cancelIv.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        doneBtn.setOnClickListener {
            bottomSheetDialog.dismiss()
        }
        bottomSheetDialog.setContentView(bottomSheetView)
        bottomSheetDialog.show()
    }
}