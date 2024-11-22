package com.example.kong_android.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R

class RecordAdapter : RecyclerView.Adapter<RecordAdapter.RecordViewHolder>() {
    private val records = listOf(
        RecordData("2024.11.30", "알바비", "60,000"),
        RecordData("2024.11.29", "용돈", "30,000"),
        RecordData("2024.11.28", "식비", "15,000")
    )

    class RecordViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val recordDate: TextView = view.findViewById(R.id.record_date_tv)
        val recordTitle: TextView = view.findViewById(R.id.record_category_tv)
        val recordAmount: TextView = view.findViewById(R.id.amount_amount_tv)
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
        holder.recordAmount.text = record.amount
    }

    override fun getItemCount(): Int {
        return records.size
    }

    // 데이터 클래스 추가
    data class RecordData(val date: String, val category: String, val amount: String)
}