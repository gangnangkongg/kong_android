package com.example.kong_android.auth

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("status") val status: Int,
    @SerializedName("message") val message: String,
    @SerializedName("data") val data: LoginData
) {
    data class LoginData(
        @SerializedName("accessToken") val accessToken: String,
        @SerializedName("refreshToken") val refreshToken: String
    )
}