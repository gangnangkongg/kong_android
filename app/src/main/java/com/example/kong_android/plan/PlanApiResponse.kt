package com.example.kong_android.plan

data class PlanApiResponse(
    val status: Int,
    val message: String,
    val data: List<GetPlanResponse>
)

data class GetPlanResponse(
    val id: Long,
    val userId: Int,
    val startDate: String,
    val endDate: String,
    val amount: Int,
    val spent: Int
)