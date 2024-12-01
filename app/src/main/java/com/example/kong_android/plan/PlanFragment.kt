package com.example.kong_android.plan

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.RetrofitClient
import com.example.kong_android.auth.SharedPreferencesManager
import com.example.kong_android.databinding.FragmentPlanBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: PlanAdapter
    private var _binding: FragmentPlanBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlanBinding.inflate(inflater, container, false)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // RecyclerView 초기화
        recyclerView = binding.recordRecyclerView
        adapter = PlanAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 플러스 버튼 클릭
        val plusButton: FloatingActionButton = binding.plusBtn
        plusButton.setOnClickListener {
            val intent = Intent(requireContext(), PlanAddActivity::class.java)
            startActivity(intent)
        }

        // 저장된 accessToken 가져오기
        token = sharedPreferencesManager.getAccessToken()

        // 토큰이 null이 아니면 데이터를 요청
        token?.let { fetchPlanData(it) }

        return binding.root
    }
    private fun fetchPlanData(token: String) {
        val authToken = "$token"

        RetrofitClient.instance.fetchPlanData(authToken)
            .enqueue(object : Callback<PlanApiResponse> {
                override fun onResponse(
                    call: Call<PlanApiResponse>,
                    response: Response<PlanApiResponse>
                ) {
                    if (response.isSuccessful) {
                        response.body()?.let { apiResponse ->
                            val plans = apiResponse.data // 실제 계획 데이터
                            adapter.updatePlans(plans)
                            Log.d("PlanFragment", "Fetched ${plans.size} plans.")
                        }
                    } else {
                        val errorMessage = response.errorBody()?.string()
                        Log.e("PlanFragment", "Error: $errorMessage")

                        // 토큰 만료 시 처리
                        if (response.code() == 401) {
                            handleTokenExpired()
                        }
                    }
                }

                override fun onFailure(call: Call<PlanApiResponse>, t: Throwable) {
                    Log.e("PlanFragment", "Network Error: ${t.message}")
                }
            })
    }

    private fun handleTokenExpired() {
        Log.e("PlanFragment", "Access token expired. Prompting user to re-login.")
        // Re-login 또는 토큰 갱신 로직
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}