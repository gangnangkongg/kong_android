package com.example.kong_android.plan

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R
import com.example.kong_android.RetrofitClient
import com.example.kong_android.auth.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanAdapter : RecyclerView.Adapter<PlanAdapter.PlanViewHolder>() {
    private val plans = mutableListOf<GetPlanResponse>()

    class PlanViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val planStartDate: TextView = view.findViewById(R.id.plan_start_date)
        val planEndDate: TextView = view.findViewById(R.id.plan_end_date)
        val planTargetAmount: TextView = view.findViewById(R.id.plan_target_amount)
        val planCurrentAmount: TextView = view.findViewById(R.id.plan_current_amount)
        val planRemainAmount: TextView = view.findViewById(R.id.plan_remain_amount)
        val deleteButton: ImageButton = view.findViewById(R.id.plan_delete_btn)
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

        holder.deleteButton.setOnClickListener {
            val token = SharedPreferencesManager(holder.itemView.context).getAccessToken()
            token?.let {
                deletePlan(it, plan.id, holder.adapterPosition)
            }
        }
    }

    private fun deletePlan(token: String, planId: Long, position: Int) {
        RetrofitClient.instance.deleteRecord(token, planId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 삭제 성공 시, 리스트에서 해당 아이템 제거
                        plans.removeAt(position)
                        notifyItemRemoved(position)
                        Log.d("PlanAdapter", "Plan with ID $planId deleted successfully.")
                    } else {
                        // 에러 응답 처리
                        Log.e("PlanAdapter", "Failed to delete plan with ID $planId.")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("PlanAdapter", "Error deleting plan: ${t.message}")
                }
            })
    }

    override fun getItemCount(): Int = plans.size

    fun updatePlans(newPlans: List<GetPlanResponse>) {
        plans.clear()
        plans.addAll(newPlans)
        notifyDataSetChanged()
    }
}