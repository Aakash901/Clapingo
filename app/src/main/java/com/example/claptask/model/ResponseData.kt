package com.example.claptask.model

data class ResponseData(
    val feedbackCategories: List<FeedbackCategory>,
    val statusCode: Int
)