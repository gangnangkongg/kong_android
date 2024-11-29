package com.example.kong_android.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.kong_android.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _homeData = MutableLiveData<HomeResponse>()
    val homeData: LiveData<HomeResponse> = _homeData

    private val _topCategoryName = MutableLiveData<String?>()
    val topCategoryName: LiveData<String?> = _topCategoryName

    // PieChart 데이터를 저장
    private val _pieChartData = MutableLiveData<Map<String, Float>>()
    val pieChartData: LiveData<Map<String, Float>> = _pieChartData

    // Retrofit 인스턴스
    private val retrofitService = RetrofitClient.instance

    // 홈 데이터 요청
    fun fetchHomeData(token: String) {
        retrofitService.fetchHomeData(token).enqueue(object : Callback<HomeResponse> {
            override fun onResponse(call: Call<HomeResponse>, response: Response<HomeResponse>) {
                if (response.isSuccessful) {
                    _homeData.value = response.body()
                } else {
                    Log.e("HomeViewModel", "Home data fetch failed: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<HomeResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Home data fetch error: ${t.message}")
            }
        })
    }

    // 분석 데이터 요청
    fun fetchAnalysisData(token: String) {
        retrofitService.fetchAnalysisData(token).enqueue(object : Callback<AnalysisResponse> {
            override fun onResponse(call: Call<AnalysisResponse>, response: Response<AnalysisResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    body?.let {
                        // 전체 금액 계산
                        val totalAmount = it.total.toFloat()

                        // 카테고리와 금액 데이터를 계산
                        val pieData = it.categories.zip(it.data) { category, value ->
                            category to (value.toFloat() / totalAmount * 100) // 비율로 변환
                        }.sortedByDescending { it.second } // 내림차순 정렬

                        // 가장 큰 금액을 차지하는 카테고리 추출
                        val topCategory = pieData.firstOrNull()?.first

                        // 카테고리명 저장
                        _topCategoryName.value = topCategory

                        // 카테고리 개수에 따른 처리
                        val topCategories: List<Pair<String, Float>>
                        val others: MutableList<Pair<String, Float>> = mutableListOf()

                        if (pieData.size > 5) {
                            topCategories = pieData.take(5)
                            val remainingCategories = pieData.drop(5)
                            val otherTotal = remainingCategories.sumOf { it.second.toDouble() }.toFloat()
                            others.add("기타" to otherTotal)
                        } else {
                            topCategories = pieData
                        }

                        // 최종 데이터 합치기
                        val finalData = topCategories + others

                        // LiveData에 결과 저장
                        _pieChartData.value = finalData.toMap()
                    }
                } else {
                    Log.e("HomeViewModel", "Analysis data fetch failed: ${response.errorBody()}")
                }
            }

            override fun onFailure(call: Call<AnalysisResponse>, t: Throwable) {
                Log.e("HomeViewModel", "Analysis data fetch error: ${t.message}")
            }
        })
    }

    // 데이터를 갱신하는 메서드
    fun updatePieChartData(data: Map<String, Float>) {
        _pieChartData.value = data
    }
}