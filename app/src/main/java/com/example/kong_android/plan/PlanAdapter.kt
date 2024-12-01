package com.example.kong_android.plan

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R

class PlanAdapter : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {
    private val plans = mutableListOf<GetPlanResponse>()

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
        holder.planTargetAmount.text = plan.amount.toString()
        holder.planCurrentAmount.text = plan.spent.toString()
        holder.planRemainAmount.text = (plan.amount - plan.spent).toString()
    }

    override fun getItemCount(): Int = plans.size

    fun updatePlans(newPlans: List<GetPlanResponse>) {
        plans.clear()
        plans.addAll(newPlans)
        notifyDataSetChanged()
    }
}