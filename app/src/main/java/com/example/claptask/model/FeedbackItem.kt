package com.example.claptask.model

data class FeedbackItem(
    val aspect: String,
    val didWell: List<String>,
    val scopeOfImprovement: List<String>
)