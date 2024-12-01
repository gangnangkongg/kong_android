package com.example.kong_android.plan

data class PlanResponse(
    val id: Long,
    val userId: Long,
    val startDate: String,
    val endDate: String,
    val amount: Long,
    val spent: Long
)