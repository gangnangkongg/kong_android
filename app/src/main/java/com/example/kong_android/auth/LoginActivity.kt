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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
                        Log.d("LoginActivity", "AccessToken: ${loginResponse.accessToken}")
                        Log.d("LoginActivity", "RefreshToken: ${loginResponse.refreshToken}")
                        Toast.makeText(this@LoginActivity, "로그인 성공", Toast.LENGTH_SHORT).show()

                        // 홈 화면으로 이동
                        val intent = Intent(this@LoginActivity, HomeActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this@LoginActivity, "응답이 비어 있습니다.", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@LoginActivity, "로그인 실패: ${response.message()}", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Log.e("LoginActivity", "로그인 요청 실패", t)
                Toast.makeText(this@LoginActivity, "로그인 요청 실패: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }
}