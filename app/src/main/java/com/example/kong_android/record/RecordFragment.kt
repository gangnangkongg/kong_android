package com.example.kong_android.record

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R
import com.example.kong_android.databinding.FragmentMyBinding
import com.example.kong_android.databinding.FragmentRecordBinding
import com.google.android.material.floatingactionbutton.FloatingActionButton

class RecordFragment: Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: RecordAdapter
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)

        /* 기록 리사이클러뷰 조회 */
        recyclerView = binding.recordRecyclerView
        adapter = RecordAdapter() // 어댑터 초기화
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        /* 플러스 버튼 클릭 시 수입/지출 등록 페이지로 이동 */
        val plusButton: FloatingActionButton = binding.plusBtn
        plusButton.setOnClickListener {
            val intent = Intent(requireContext(), RecordAddActivity::class.java)
            startActivity(intent)
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}