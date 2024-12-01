package com.example.kong_android.plan

data class PlanRequest(
    val startDate: String,
    val endDate: String,
    val amount: Long
)