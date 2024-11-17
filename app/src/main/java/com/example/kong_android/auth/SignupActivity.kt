package com.example.kong_android.auth

import android.content.Intent
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.R

class SignupActivity : AppCompatActivity() {
    private lateinit var idEdit: EditText
    private lateinit var pwEdit: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        idEdit = findViewById(R.id.id_edit)
        pwEdit = findViewById(R.id.pw_edit)
        val backButton: ImageButton = findViewById(R.id.back_btn)
        val SignupBtn: AppCompatButton = findViewById(R.id.signup_btn)
        val LoginTextBtn: AppCompatButton = findViewById(R.id.login_text_btn)

        // 회원가입 버튼 클릭 시 확인 후 홈화면으로 이동

        // 로그인 텍스트 클릭 시 로그인 페이지로 이동
        LoginTextBtn.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 중복확인 버튼 클릭 시 아이디 중복 확인

        // 뒤로가기 버튼 클릭 시 현 화면 종료
        backButton.setOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
    }
}