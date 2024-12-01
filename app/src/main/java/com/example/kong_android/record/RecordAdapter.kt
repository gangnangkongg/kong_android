package com.example.kong_android.record

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

class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    private var records: List<GetHistoryResponse> = emptyList()

    fun updateRecords(newRecords: List<GetHistoryResponse>) {
        records = newRecords
        notifyDataSetChanged()
    }

    class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recordDate: TextView = view.findViewById(R.id.record_date_tv)
        val recordTitle: TextView = view.findViewById(R.id.record_category_tv)
        val recordAmount: TextView = view.findViewById(R.id.amount_amount_tv)
        val recordType: TextView = view.findViewById(R.id.amount_type_tv)
        val layout: View = view.findViewById(R.id.record_recycler_layout)
        val deleteButton: ImageButton = view.findViewById(R.id.record_delete_btn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.record_recycler_view, parent, false)
        return RecordViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {
        val record = records[position]

        holder.recordDate.text = record.date
        holder.recordTitle.text = record.category
        holder.recordAmount.text = "${record.amount} 원"

        if (record.type == "INCOME") {
            holder.layout.setBackgroundResource(R.drawable.record_green_layout)
            holder.recordDate.setTextColor(holder.itemView.context.getColor(R.color.main_green))
            holder.recordTitle.setTextColor(holder.itemView.context.getColor(R.color.main_green))
            holder.recordAmount.setTextColor(holder.itemView.context.getColor(R.color.main_green))
            holder.recordType.setTextColor(holder.itemView.context.getColor(R.color.main_green))
            holder.deleteButton.setColorFilter(holder.itemView.context.getColor(R.color.main_green))
            holder.recordType.text = "+"
        } else {
            holder.layout.setBackgroundResource(R.drawable.record_red_layout)
            holder.recordDate.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordTitle.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordAmount.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordType.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.deleteButton.setColorFilter(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordType.text = "-"
        }

        // 삭제 버튼 클릭 이벤트 처리
        holder.deleteButton.setOnClickListener {
            val token = SharedPreferencesManager(holder.itemView.context).getAccessToken()
            token?.let {
                // 서버에 삭제 요청
                deleteRecord(it, record.id, holder.adapterPosition)
            }
        }
    }
    private fun deleteRecord(token: String, recordId: Long, position: Int) {
        RetrofitClient.instance.deleteRecord(token, recordId)
            .enqueue(object : Callback<Void> {
                override fun onResponse(call: Call<Void>, response: Response<Void>) {
                    if (response.isSuccessful) {
                        // 삭제 성공 시, 리스트에서 해당 아이템 제거
                        records = records.toMutableList().apply { removeAt(position) }
                        notifyItemRemoved(position)
                        Log.d("RecordAdapter", "Record with ID $recordId deleted successfully.")
                    } else {
                        // 에러 응답 처리
                        Log.e("RecordAdapter", "Failed to delete record with ID $recordId.")
                    }
                }

                override fun onFailure(call: Call<Void>, t: Throwable) {
                    // 네트워크 오류 처리
                    Log.e("RecordAdapter", "Error deleting record: ${t.message}")
                }
            })
    }

    override fun getItemCount(): Int = records.size
}