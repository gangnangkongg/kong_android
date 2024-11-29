package com.example.kong_android.auth

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.example.kong_android.R
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.RetrofitClient
import com.example.kong_android.home.HomeActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {
    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText
    private lateinit var sharedPreferencesManager: SharedPreferencesManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferencesManager = SharedPreferencesManager(this)

        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        val backButton: ImageButton = findViewById(R.id.back_btn)
        val loginBtn: AppCompatButton = findViewById(R.id.login_btn)
        val SignupTextBtn: AppCompatButton = findViewById(R.id.signup_text_btn)

        // 로그인 버튼 클릭 시 확인 후 홈화면으로 이동
        loginBtn.setOnClickListener {
            val username = idEdit.text.toString()
            val password = pwEdit.text.toString()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                performLogin(username, password)
            } else {
                Toast.makeText(this, "아이디와 비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show()
            }
        }

        // 회원가입 텍스트 클릭 시 회원가입 페이지로 이동
        SignupTextBtn.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }

        // 뒤로가기 버튼 클릭 시 현 화면 종료
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun performLogin(username: String, password: String) {
        val loginRequest = LoginRequest(username, password)
        val call = RetrofitClient.instance.login(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        val status = loginResponse.status
                        val accessToken = loginResponse.data.accessToken
                        val refreshToken = loginResponse.data.refreshToken

                        // 디버깅용 로그
                        Log.d(
                            "LoginActivity",
                            "status: $status, accessToken: $accessToken, refreshToken: $refreshToken"
                        )

                        // status가 200일 때 로그인 성공
                        if (status == 200 && !accessToken.isNullOrEmpty() && !refreshToken.isNullOrEmpty()) {
                            Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                            // 로그인 후 토큰 저장
                            sharedPreferencesManager.saveTokens(accessToken, refreshToken)

                            // 홈화면으로 이동
                            startActivity(Intent(this@LoginActivity, HomeActivity::class.java))
                            finish()
                        } else {
                            Toast.makeText(this@LoginActivity, "로그인 실패: 잘못된 응답", Toast.LENGTH_SHORT)
                                .show()
                        }
                    } else {
                        Toast.makeText(this@LoginActivity, "응답이 비어있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    // 로그인 실패 시 상태 코드에 따른 처리
                    when (response.code()) {
                        400 -> Toast.makeText(
                            this@LoginActivity,
                            "비밀번호가 일치하지 않습니다.",
                            Toast.LENGTH_SHORT
                        ).show()

                        401 -> Toast.makeText(
                            this@LoginActivity,
                            "권한이 없습니다. 다시 시도해주세요.",
                            Toast.LENGTH_SHORT
                        ).show()

                        else -> Toast.makeText(
                            this@LoginActivity,
                            "알 수 없는 오류가 발생했습니다.",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "로그인 실패", t)
                Toast.makeText(this@LoginActivity, "서버와 연결할 수 없습니다. 다시 시도해주세요.", Toast.LENGTH_SHORT)
                    .show()
            }
        })
    }
}