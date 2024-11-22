package com.example.kong_android.record

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.R

class RecordAddActivity : AppCompatActivity() {
    private lateinit var dateEdit: EditText
    private lateinit var amountEdit: EditText
    private lateinit var categoryEdit: EditText
    private lateinit var importTypeBtn: RadioButton
    private lateinit var outlayTypeBtn: RadioButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_record_add)

        dateEdit = findViewById(R.id.date_edit)
        amountEdit = findViewById(R.id.amount_edit)
        categoryEdit = findViewById(R.id.category_edit)

        val backButton: ImageButton = findViewById(R.id.back_btn)
        val recordAddBtn: AppCompatButton = findViewById(R.id.record_add_btn)

        importTypeBtn = findViewById(R.id.import_type_btn)
        outlayTypeBtn = findViewById(R.id.outlay_type_btn)

        val typeBtnLayout = findViewById<RadioGroup>(R.id.type_btn_layout)

        /* 뒤록가기 버튼 클릭 시 현재 페이지 종료 */
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }

        /* 수입 지출 여부 선택 시 스타일 변경 */
        typeBtnLayout.setOnCheckedChangeListener { _, checkedId ->
            when(checkedId){
                R.id.import_type_btn -> {
                    // 수입 선택 시 스타일 변경
                    importTypeBtn.setTextColor(resources.getColor(R.color.main_green))
                    importTypeBtn.setBackgroundResource(R.drawable.type_green_layout)

                    outlayTypeBtn.setTextColor(resources.getColor(R.color.deep_gray))
                    outlayTypeBtn.setBackgroundResource(R.drawable.type_soft_layout)
                }
                R.id.outlay_type_btn -> {
                    // 지출 선택 시 스타일 변경
                    outlayTypeBtn.setTextColor(resources.getColor(R.color.deep_red))
                    outlayTypeBtn.setBackgroundResource(R.drawable.type_red_layout)

                    importTypeBtn.setTextColor(resources.getColor(R.color.deep_gray))
                    importTypeBtn.setBackgroundResource(R.drawable.type_soft_layout)
                }
            }
        }

        /* 등록하기 버튼 클릭 시 서버에 전송 */
        // 수입/지출 타입 여부 저장

    }
}