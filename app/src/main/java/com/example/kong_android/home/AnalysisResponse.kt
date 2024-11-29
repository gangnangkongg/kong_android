package com.example.kong_android.home

data class AnalysisResponse(
    val total: Long,
    val categories: List<String>,
    val data: List<Long>
)