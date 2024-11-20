package com.example.kong_android.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.util.Locale

class HomeViewModel : ViewModel() {

    // PieChart 데이터를 저장할 LiveData
    private val _pieChartData = MutableLiveData<Map<String, Float>>()
    val pieChartData: LiveData<Map<String, Float>> = _pieChartData

    init {
        // 예시 데이터
        _pieChartData.value = mapOf(
            "식음료" to 100f,
            "쇼핑" to 70f,
            "교통" to 50f,
            "이체" to 30f,
            "여가" to 20f,
            "..." to 10f
        )
    }

    // 데이터를 갱신하는 메서드
    fun updatePieChartData(data: Map<String, Float>) {
        _pieChartData.value = data
    }
}