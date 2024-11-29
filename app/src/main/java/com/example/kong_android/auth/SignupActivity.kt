package com.example.kong_android.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.R
import com.example.kong_android.RetrofitClient
import com.example.kong_android.RetrofitService
import com.example.kong_android.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SignupActivity : AppCompatActivity() {
    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        val backButton: ImageButton = findViewById(R.id.back_btn)
        val signupBtn: AppCompatButton = findViewById(R.id.signup_btn)
        val LoginTextBtn: AppCompatButton = findViewById(R.id.login_text_btn)

        sharedPreferencesManager = SharedPreferencesManager(this)

        // 회원가입 버튼 클릭 시 확인 후 홈화면으로 이동
        signupBtn.setOnClickListener {
            val username = idEdit.text.toString().trim()
            val password = pwEdit.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                signup(SignupRequest(username, password))
            } else {
                // 유효성 검사 실패 시 메시지 표시
                Log.e("SignupActivity", "아이디와 비밀번호를 입력하세요.")
            }
        }

        // 로그인 텍스트 클릭 시 로그인 페이지로 이동
        LoginTextBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼 클릭 시 현 화면 종료
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun signup(request: SignupRequest) {
        val call = RetrofitClient.instance.registerUser(request)
        call.enqueue(object : Callback<SignupResponse> {
            override fun onResponse(call: Call<SignupResponse>, response: Response<SignupResponse>) {
                if (response.isSuccessful) {
                    response.body()?.let {
                        Log.d("SignupActivity", "Response Body: $it")
                        val accessToken = it.data.accessToken
                        val refreshToken = it.data.refreshToken

                        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
                            Log.e("SignupActivity", "토큰 값이 누락되었습니다. accessToken: $accessToken, refreshToken: $refreshToken")
                            return
                        }

                        sharedPreferencesManager.saveTokens(accessToken, refreshToken)
                        Log.d("SignupActivity", "회원가입 성공. 토큰 저장 완료.")
                        val intent = Intent(this@SignupActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                } else {
                    Log.e("SignupActivity", "회원가입 실패: ${response.code()}")
                }
            }


            override fun onFailure(call: Call<SignupResponse>, t: Throwable) {
                Log.e("SignupActivity", "회원가입 요청 실패: ${t.message}")
            }
        })
    }
}