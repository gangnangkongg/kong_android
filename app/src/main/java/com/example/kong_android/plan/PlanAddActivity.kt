package com.example.kong_android.plan

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.R

class PlanAddActivity: AppCompatActivity() {
    private lateinit var startDateEdit: EditText
    private lateinit var endDateEdit: EditText
    private lateinit var targetAmountEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_add)

        startDateEdit = findViewById(R.id.start_date_edit)
        endDateEdit = findViewById(R.id.end_date_edit)
        targetAmountEdit = findViewById(R.id.target_amount_edit)

        val backButton: ImageButton = findViewById(R.id.back_btn)
        val planAddBtn: AppCompatButton = findViewById(R.id.plan_add_btn)

        /* 뒤록가기 버튼 클릭 시 현재 페이지 종료 */
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        /* 등록하기 버튼 클릭 시 서버에 전송 */
    }
}