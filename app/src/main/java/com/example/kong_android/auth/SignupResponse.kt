package com.example.kong_android.auth

import com.google.gson.annotations.SerializedName

data class SignupResponse(
    val status: Int,
    val message: String,
    val data: TokenData
)

data class TokenData(
    @SerializedName("accessToken") val accessToken: String,
    @SerializedName("refreshToken") val refreshToken: String
)