package com.example.kong_android.auth

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kong_android.R
import android.widget.EditText
import android.widget.ImageButton
import androidx.appcompat.widget.AppCompatButton

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
}