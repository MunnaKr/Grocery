package com.munna.kmptesting

interface SessionManager {
    fun saveUserName(userName: String)
    fun getUserName(): String
    fun clear()
}

expect fun createSessionManager(context: Any? = null): SessionManager
