package com.example.kong_android.my

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kong_android.R
import com.example.kong_android.auth.SharedPreferencesManager
import com.example.kong_android.databinding.FragmentMyBinding
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

class MyFragment: Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var myViewModel: MyViewModel
    private var _binding: FragmentMyBinding? = null
    private val binding get() = _binding!!

    private lateinit var sharedPreferencesManager: SharedPreferencesManager
    private var token: String? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)

        // ViewModel 초기화
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        // SharedPreferencesManager 초기화
        sharedPreferencesManager = SharedPreferencesManager(requireContext())

        // 저장된 accessToken을 가져오기
        token = sharedPreferencesManager.getAccessToken()

        // 토큰이 null이 아니면 데이터를 요청
        token?.let {
            myViewModel.fetchMyData(it)
            myViewModel.fetchAnalysisData(it)
        }

        pieChart = binding.categoryChartView

        // 내 데이터 호출
        myViewModel.myData.observe(viewLifecycleOwner) { myData ->
            binding.totalTv.text = myData.total.toString()
        }

        // 분석 데이터 호출
        myViewModel.pieChartData.observe(viewLifecycleOwner) { pieData ->
            setUpPieChart(pieData)
        }

        // 가장 큰 금액을 가진 카테고리명 설정
        myViewModel.topCategoryName.observe(viewLifecycleOwner) { topCategory ->
            topCategory?.let {
                binding.categoryTypeTv.text = it
            }
        }

        return binding.root
    }

    private fun setUpPieChart(data: Map<String, Float>) {
        val entries = ArrayList<PieEntry>()

        // 데이터를 PieEntry로 변환
        for ((label, value) in data) {
            if (value > 0) {
                entries.add(PieEntry(value, label))
            }
        }

        if (entries.isNotEmpty()) {
            val dataSet = PieDataSet(entries, "Categories")
            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

            val customColors = listOf(
                ContextCompat.getColor(requireContext(), R.color.green_1),
                ContextCompat.getColor(requireContext(), R.color.green_2),
                ContextCompat.getColor(requireContext(), R.color.green_3),
                ContextCompat.getColor(requireContext(), R.color.green_4),
                ContextCompat.getColor(requireContext(), R.color.green_5),
                ContextCompat.getColor(requireContext(), R.color.green_6)
            )
            dataSet.colors = customColors

            val pieData = PieData(dataSet)
            pieChart.data = pieData

            pieChart.description = Description().apply {
                text = "Category Distribution"
                isEnabled = false
            }
            pieChart.invalidate()

            pieChart.isDrawHoleEnabled = true
            pieChart.setHoleColor(android.graphics.Color.WHITE)
            pieChart.setTransparentCircleColor(android.graphics.Color.WHITE)
            pieChart.setCenterText("지출 분석")
            pieChart.setCenterTextSize(15f)
            pieChart.legend.isEnabled = false
            dataSet.valueTextSize = 10f
            dataSet.valueTextColor = android.graphics.Color.WHITE
            pieChart.setEntryLabelTypeface(ResourcesCompat.getFont(requireContext(), R.font.pretendard_bold))
            pieChart.setDrawSlicesUnderHole(true)
        } else {
            Log.e("PieChart", "Data is empty or invalid.")
        }
    }
}