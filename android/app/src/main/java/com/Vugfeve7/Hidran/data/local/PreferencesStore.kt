package com.Vugfeve7.Hidran.data.local

import android.content.Context
import android.content.SharedPreferences

class PreferencesStore(context: Context) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences(STORE_NAME, Context.MODE_PRIVATE)

    fun readInt(key: String, fallback: Int): Int = preferences.getInt(key, fallback)

    fun writeInt(key: String, value: Int) {
        preferences.edit().putInt(key, value).apply()
    }

    fun readBoolean(key: String, fallback: Boolean): Boolean = preferences.getBoolean(key, fallback)

    fun writeBoolean(key: String, value: Boolean) {
        preferences.edit().putBoolean(key, value).apply()
    }

    fun readText(key: String, fallback: String): String =
        preferences.getString(key, fallback) ?: fallback

    fun writeText(key: String, value: String) {
        preferences.edit().putString(key, value).apply()
    }

    fun wipe() {
        preferences.edit().clear().apply()
    }

    companion object {
        private const val STORE_NAME = "seven_stars_store"
    }
}
