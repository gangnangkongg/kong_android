package com.example.kong_android.record

data class RecordRequest(
    val date: String,
    val amount: Long,
    val category: String,
    val type: String
)
