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
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyBinding.inflate(inflater, container, false)

        /* 사용자 ID 조회 */

        /* 11월 합계 금액 조회 */

        /* 11월 카테고리 조회 */
        // PieChart 초기화
        pieChart = binding.categoryChartView

        // MyViewModel 초기화
        myViewModel = ViewModelProvider(this).get(MyViewModel::class.java)

        // ViewModel에서 데이터를 받아와서 차트에 적용
        myViewModel.pieChartData.observe(viewLifecycleOwner, { data ->
            setUpPieChart(data)
        })

        return binding.root
    }

    private fun setUpPieChart(data: Map<String, Float>) {
        val entries = ArrayList<PieEntry>()

        // 데이터를 PieEntry로 변환
        for ((label, value) in data) {
            if (value > 0) { // 값이 0보다 큰 경우에만 PieEntry 추가
                entries.add(PieEntry(value, label))
            }
        }

        if (entries.isNotEmpty()) { // 데이터가 비어있지 않은 경우에만 차트 업데이트
            // PieDataSet 생성
            val dataSet = PieDataSet(entries, "Categories")
            dataSet.colors = ColorTemplate.COLORFUL_COLORS.toList()

            // 사용자가 지정한 색상 리스트
            val customColors = listOf(
                ContextCompat.getColor(requireContext(), R.color.green_1),
                ContextCompat.getColor(requireContext(), R.color.green_2),
                ContextCompat.getColor(requireContext(), R.color.green_3),
                ContextCompat.getColor(requireContext(), R.color.green_4),
                ContextCompat.getColor(requireContext(), R.color.green_5),
                ContextCompat.getColor(requireContext(), R.color.green_6)
            )
            dataSet.colors = customColors

            // PieData 생성
            val pieData = PieData(dataSet)
            pieChart.data = pieData

            // 차트 스타일 설정
            pieChart.description = Description().apply {
                text = "Category Distribution"
                isEnabled = false  // Description 비활성화 (필요에 따라 조정)
            }
            pieChart.invalidate() // 차트 갱신

            // 추가적인 차트 스타일 설정
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