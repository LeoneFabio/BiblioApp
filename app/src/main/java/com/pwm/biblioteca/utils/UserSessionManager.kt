package com.pwm.biblioteca.utils

import android.content.Context
import android.content.SharedPreferences

class UserSessionManager(context: Context) {

    companion object {
        private const val PREF_NAME = "UserSession"
        private const val KEY_USERNAME = "username"
        private const val KEY_PASSWORD = "password"
        private const val KEY_THEME = "theme"
    }

    private val sharedPreferences: SharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)

    fun saveUserCredentials(username: String, password: String) {
        val editor = sharedPreferences.edit()
        editor.putString(KEY_USERNAME, username)
        editor.putString(KEY_PASSWORD, password)
        editor.apply()
    }

    fun clearUserCredentials() {
        val editor = sharedPreferences.edit()
        editor.remove(KEY_USERNAME)
        editor.remove(KEY_PASSWORD)
        editor.apply()
    }

    fun isUserLoggedIn(): Boolean {
        val username = sharedPreferences.getString(KEY_USERNAME, null)
        val password = sharedPreferences.getString(KEY_PASSWORD, null)

        return !username.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    fun getUsername(): String? {
        return sharedPreferences.getString(KEY_USERNAME, null)
    }

    fun getPassword(): String? {
        return sharedPreferences.getString(KEY_PASSWORD, null)
    }


    fun saveTheme(isDarkModeEnabled: Boolean) {
        val editor = sharedPreferences.edit()
        editor.putBoolean(KEY_THEME, isDarkModeEnabled)
        editor.apply()
    }

    fun isDarkModeEnabled(): Boolean {
        return sharedPreferences.getBoolean(KEY_THEME, false)
    }

}
