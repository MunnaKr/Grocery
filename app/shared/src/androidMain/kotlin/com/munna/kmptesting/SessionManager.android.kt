package com.munna.kmptesting

import android.content.Context
import android.content.SharedPreferences

class AndroidSessionManager(context: Context) : SessionManager {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)

    override fun saveUserName(userName: String) {
        prefs.edit().putString("user_name", userName).apply()
    }

    override fun getUserName(): String {
        return prefs.getString("user_name", "") ?: ""
    }

    override fun clear() {
        prefs.edit().clear().apply()
    }
}

actual fun createSessionManager(context: Any?): SessionManager {
    val androidContext = context as? Context ?: throw IllegalArgumentException("Android Context required for SessionManager")
    return AndroidSessionManager(androidContext)
}
