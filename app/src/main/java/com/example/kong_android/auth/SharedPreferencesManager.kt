package com.example.kong_android.auth

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferencesManager(context: Context) {

    private val PREFS_FILENAME = "com.example.kong_android.prefs"
    private val PREF_ACCESS_TOKEN = "accessToken"
    private val PREF_REFRESH_TOKEN = "refreshToken"

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)
    private val editor: SharedPreferences.Editor = sharedPreferences.edit()

    fun saveTokens(accessToken: String?, refreshToken: String?) {
        if (accessToken.isNullOrEmpty() || refreshToken.isNullOrEmpty()) {
            Log.e("SharedPreferencesManager", "토큰 값이 null 또는 비어 있습니다.")
            return
        }

        editor.putString(PREF_ACCESS_TOKEN, accessToken)
        editor.putString(PREF_REFRESH_TOKEN, refreshToken)
        editor.apply()
    }

    fun getAccessToken(): String? {
        return sharedPreferences.getString(PREF_ACCESS_TOKEN, null)
    }

    fun getRefreshToken(): String? {
        return sharedPreferences.getString(PREF_REFRESH_TOKEN, null)
    }

    fun clearTokens() {
        editor.remove(PREF_ACCESS_TOKEN)
        editor.remove(PREF_REFRESH_TOKEN)
        editor.apply()
    }
}
