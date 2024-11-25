package com.example.kong_android

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.AppCompatButton
import com.example.kong_android.auth.LoginActivity
import com.example.kong_android.auth.SignupActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val loginButton: AppCompatButton = findViewById(R.id.login_btn)
        val signupButton: AppCompatButton = findViewById(R.id.signup_btn)
        val pingButton: AppCompatButton = findViewById(R.id.ping_btn)
        val pingImage: ImageView = findViewById(R.id.ping_img)

        // 로고 중앙 버튼 클릭 시 이미지 출력
        pingButton.setOnClickListener {
            if (pingImage.visibility == View.GONE) {
                pingImage.visibility = View.VISIBLE
            } else {
                pingImage.visibility = View.GONE
            }
        }

        // 로그인 버튼 클릭 시 로그인 페이지로 이동
        loginButton.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        // 회원가입 버튼 클릭 시 회원가입 페이지로 이동
        signupButton.setOnClickListener {
            val intent = Intent(this, SignupActivity::class.java)
            startActivity(intent)
        }
    }
}