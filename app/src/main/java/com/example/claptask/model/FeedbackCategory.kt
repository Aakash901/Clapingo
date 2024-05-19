package com.example.claptask.model

data class FeedbackCategory(
    val categoryName: String,
    val feedbackItems: List<FeedbackItem>
)