package com.example.kong_android.record

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R
import com.example.kong_android.RetrofitClient
import com.example.kong_android.auth.LoginActivity
import com.example.kong_android.auth.SharedPreferencesManager
import com.example.kong_android.databinding.FragmentMyBinding
import com.example.kong_android.databinding.FragmentRecordBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton
import retrofit2.Call

class RecordFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecordAdapter
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // RecyclerView 초기화
        recyclerView = binding.recordRecyclerView
        adapter = RecordAdapter()
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // 플러스 버튼 클릭
        val plusButton: FloatingActionButton = binding.plusBtn
        plusButton.setOnClickListener {
            val intent = Intent(requireContext(), RecordAddActivity::class.java)
            startActivity(intent)
        }

        // 저장된 accessToken 가져오기
        token = sharedPreferencesManager.getAccessToken()

        // 토큰이 null이 아니면 데이터를 요청
        token?.let { fetchHistoryData(it) }

        return binding.root
    }

    private fun fetchHistoryData(token: String) {
        val token = "$token"

        RetrofitClient.instance.fetchHistoryData(token)
            .enqueue(object : retrofit2.Callback<List<GetHistoryResponse>> {
                override fun onResponse(
                    call: Call<List<GetHistoryResponse>>,
                    response: retrofit2.Response<List<GetHistoryResponse>>
                ) {
                    if (response.isSuccessful) {
                        // 응답 데이터가 성공적으로 받아졌을 때 처리
                        response.body()?.let { records ->
                            adapter.updateRecords(records)
                            Log.d("RecordFragment", "Fetched ${records.size} records.")
                        }
                    } else {
                        // 에러 응답 처리
                        val errorMessage = response.errorBody()?.string()
                        Log.e("RecordFragment", "Error: $errorMessage")

                        // 토큰 만료일 경우 리프레시 로직 추가 (필요 시)
                        if (response.code() == 401) {
                            handleTokenExpired()
                        }
                    }
                }

                override fun onFailure(call: Call<List<GetHistoryResponse>>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("RecordFragment", "Network Error: ${t.message}")
                }
            })
    }

    // 토큰 만료 처리
    private fun handleTokenExpired() {
        Log.e("RecordFragment", "Access token expired. Prompting user to re-login.")
        // 토큰 만료 처리 (로그인 화면 이동 또는 리프레시)
        val intent = Intent(requireContext(), LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}