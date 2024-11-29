package com.example.kong_android.record

data class GetHistoryResponse(
    val id: Long,
    val userId: Long,
    val date: String,
    val amount: Long,
    val category: String,
    val type: String
)
