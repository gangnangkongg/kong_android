package com.example.kong_android.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.anychart.AnyChartView
import com.example.kong_android.R
import com.example.kong_android.databinding.FragmentHomeBinding
import androidx.navigation.fragment.findNavController
import com.anychart.AnyChart
import com.anychart.chart.common.dataentry.ValueDataEntry
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.data.PieData

class HomeFragment : Fragment() {

    private lateinit var pieChart: PieChart
    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)

        /* 11월 합계 금액 더보기 버튼 클릭 시 기록 페이지로 이동 */
        binding.totalNextBtn.setOnClickListener {
            findNavController().navigate(R.id.navigation_record)
        }

        /* 11월 수입 조회 */

        /* 11월 지출 조회 */

        /* 11월 합계 금액 조회 */

        /* 11월 카테고리 조회 */
        // PieChart 초기화
        pieChart = binding.categoryChartView

        // HomeViewModel 초기화
        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        // ViewModel에서 데이터를 받아와서 차트에 적용
        homeViewModel.pieChartData.observe(viewLifecycleOwner, { data ->
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
            pieChart.setCenterTextSize(12f)
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