package com.example.kong_android.plan

import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.R
import com.example.kong_android.RetrofitClient
import com.example.kong_android.auth.SharedPreferencesManager
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PlanAddActivity: AppCompatActivity() {
    private lateinit var startDateEdit: EditText
    private lateinit var endDateEdit: EditText
    private lateinit var targetAmountEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plan_add)

        // View 초기화
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
        planAddBtn.setOnClickListener {
            val startDate = startDateEdit.text.toString()
            val endDate = endDateEdit.text.toString()
            val targetAmount = targetAmountEdit.text.toString().toLongOrNull()

            // 입력값 검증
            if (startDate.isEmpty() || endDate.isEmpty() || targetAmount == null) {
                Toast.makeText(this, "모든 필드를 올바르게 입력해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 요청 데이터 생성
            val planRequest = PlanRequest(startDate, endDate, targetAmount)

            // SharedPreferences에서 토큰 가져오기
            val sharedPreferencesManager = SharedPreferencesManager(this)
            val token = sharedPreferencesManager.getAccessToken()

            if (token.isNullOrEmpty()) {
                Toast.makeText(this, "유효한 토큰이 없습니다. 다시 로그인해주세요.", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // 서버 요청
            RetrofitClient.instance.addPlan("$token", planRequest)
                .enqueue(object : Callback<PlanResponse> {
                    override fun onResponse(call: Call<PlanResponse>, response: Response<PlanResponse>) {
                        if (response.isSuccessful) {
                            Toast.makeText(this@PlanAddActivity, "계획이 성공적으로 추가되었습니다.", Toast.LENGTH_SHORT).show()
                            finish() // 페이지 종료
                        } else {
                            Log.e("APIError", "Error: ${response.code()}, ${response.errorBody()?.string()}")
                            Toast.makeText(this@PlanAddActivity, "추가 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                        }
                    }

                    override fun onFailure(call: Call<PlanResponse>, t: Throwable) {
                        Log.e("APIError", "Failure: ${t.message}")
                        Toast.makeText(this@PlanAddActivity, "서버 연결 실패", Toast.LENGTH_SHORT).show()
                    }
                })
        }
    }
}