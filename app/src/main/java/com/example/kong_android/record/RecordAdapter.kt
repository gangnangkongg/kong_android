package com.example.kong_android.record

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.kong_android.R

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
            holder.recordType.text = "+"
        } else {
            holder.layout.setBackgroundResource(R.drawable.record_red_layout)
            holder.recordDate.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordTitle.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordAmount.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordType.setTextColor(holder.itemView.context.getColor(R.color.deep_red))
            holder.recordType.text = "-"
        }
    }
    override fun getItemCount(): Int = records.size
}