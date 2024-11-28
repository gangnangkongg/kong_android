package com.example.kong_android.auth

data class LoginResponse(
    val accessToken: String,
    val refreshToken: String
)