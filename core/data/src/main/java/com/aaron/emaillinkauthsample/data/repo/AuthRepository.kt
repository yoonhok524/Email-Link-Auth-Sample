package com.aaron.emaillinkauthsample.data.repo

import android.content.SharedPreferences
import javax.inject.Inject

class AuthRepository @Inject constructor(
    private val pref: SharedPreferences
) {

    fun saveEmail(email: String) {
        pref.edit().putString(KEY_EMAIL, email).apply()
    }

    fun getEmail(): String {
        return pref.getString(KEY_EMAIL, "") ?: ""
    }

    companion object {
        private const val KEY_EMAIL = "key_email"
    }
}