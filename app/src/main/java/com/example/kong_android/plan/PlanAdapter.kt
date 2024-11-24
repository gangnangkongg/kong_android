package com.example.kong_android.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R

class PlanAdapter : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {
    private val plans = listOf(
        PlanData("2024.11.01", "2024.11.07", "50,000", "30,000", "20,000"),
        PlanData("2024.11.01", "2024.11.30", "250,000", "30,000", "220,000")
    )

    class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planStartDate: TextView = view.findViewById(R.id.plan_start_date)
        val planEndDate: TextView = view.findViewById(R.id.plan_end_date)
        val planTargetAmount: TextView = view.findViewById(R.id.plan_target_amount)
        val planCurrentAmount: TextView = view.findViewById(R.id.plan_current_amount)
        val planRemainAmount: TextView = view.findViewById(R.id.plan_remain_amount)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlanViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.plan_recycler_view, parent, false)
        return PlanViewHolder(view)
    }

    override fun onBindViewHolder(holder: PlanViewHolder, position: Int) {
        val plan = plans[position]
        holder.planStartDate.text = plan.startDate
        holder.planEndDate.text = plan.endDate
        holder.planTargetAmount.text = plan.targetAmount
        holder.planCurrentAmount.text = plan.currentAmount
        holder.planRemainAmount.text = plan.remainAmount
    }

    override fun getItemCount(): Int {
        return plans.size
    }

    // 데이터 클래스 추가
    data class PlanData(val startDate: String, val endDate: String, val targetAmount: String, val currentAmount: String, val remainAmount: String )
}