package com.example.kong_android.record

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.R
import com.example.kong_android.RetrofitClient
import com.example.kong_android.auth.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

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
            when (checkedId) {
                R.id.import_type_btn -> {
                    importTypeBtn.setTextColor(resources.getColor(R.color.main_green))
                    importTypeBtn.setBackgroundResource(R.drawable.type_green_layout)
                    outlayTypeBtn.setTextColor(resources.getColor(R.color.deep_gray))
                    outlayTypeBtn.setBackgroundResource(R.drawable.type_soft_layout)
                }
                R.id.outlay_type_btn -> {
                    outlayTypeBtn.setTextColor(resources.getColor(R.color.deep_red))
                    outlayTypeBtn.setBackgroundResource(R.drawable.type_red_layout)
                    importTypeBtn.setTextColor(resources.getColor(R.color.deep_gray))
                    importTypeBtn.setBackgroundResource(R.drawable.type_soft_layout)
                }
            }
        }

        /* 등록하기 버튼 클릭 시 서버에 전송 */
        recordAddBtn.setOnClickListener {
            val date = dateEdit.text.toString()
            val amount = amountEdit.text.toString().toLongOrNull()
            val category = categoryEdit.text.toString()
            val type = when (typeBtnLayout.checkedRadioButtonId) {
                R.id.import_type_btn -> "INCOME"
                R.id.outlay_type_btn -> "SPEND"
                else -> null
            }

            if (date.isEmpty() || amount == null || category.isEmpty() || type == null) {
                Toast.makeText(this, "모든 필드를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 요청 데이터 생성
            val recordRequest = RecordRequest(date, amount, category, type)

            // 서버 요청 (토큰은 SharedPreferences에서 가져옴)
            val sharedPreferencesManager = SharedPreferencesManager(this)
            val token = "${sharedPreferencesManager.getAccessToken()}"

            RetrofitClient.instance.addRecord(token, recordRequest)
                .enqueue(object : Callback<RecordResponse> {
                    override fun onResponse(
                        call: Call<RecordResponse>,
                        response: Response<RecordResponse>
                    ) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@RecordAddActivity, "기록이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            finish() // 페이지 종료
                        } else {
                            Log.e("APIError", "Error: ${response.code()}, ${response.errorBody()?.string()}")
                            Toast.makeText(this@RecordAddActivity, "추가 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<RecordResponse>, t: Throwable) {
                        Log.e("APIError", "Failure: ${t.message}")
                        Toast.makeText(this@RecordAddActivity, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}