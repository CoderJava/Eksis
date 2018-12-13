package com.ysn.eksis;

import android.content.Context
import android.content.SharedPreferences

class SharedPreferencesManager constructor(context: Context) {

    private lateinit var sharedPreferences: SharedPreferences

    companion object {
        const val PREF_NAME = "pref_name"
        const val ACCESS_TOKEN: String = "access_token"
    }

    init {
        sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }

    fun putDataInt(key: String, value: Int) = sharedPreferences.edit().putInt(key, value).apply()

    fun getDataInt(key: String, defaultValue: Int = 0) = sharedPreferences.getInt(key, defaultValue)

    fun putDataBoolean(key: String, value: Boolean) = sharedPreferences.edit().putBoolean(key, value).apply()

    fun getDataBoolean(key: String, defaultValue: Boolean = false) = sharedPreferences.getBoolean(key, defaultValue)

    fun putDataFloat(key: String, value: Float) = sharedPreferences.edit().putFloat(key, value).apply()

    fun getDataFloat(key: String, defaultValue: Float = 0F) = sharedPreferences.getFloat(key, defaultValue)

    fun putDataLong(key: String, value: Long) = sharedPreferences.edit().putLong(key, value).apply()

    fun getDataLong(key: String, defaultValue: Long = 0L) = sharedPreferences.getLong(key, defaultValue)

    fun putDataString(key: String, value: String) = sharedPreferences.edit().putString(key, value).apply()

    fun getDataString(key: String, defaultValue: String = "") = sharedPreferences.getString(key, defaultValue)

    fun clearData(key: String) = sharedPreferences.edit().remove(key).apply()

    fun clearAllData() = sharedPreferences.edit().clear().apply()

}