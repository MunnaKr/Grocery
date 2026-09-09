package com.munna.kmptesting

import platform.Foundation.NSUserDefaults

class IosSessionManager : SessionManager {
    private val defaults = NSUserDefaults.standardUserDefaults

    override fun saveUserName(userName: String) {
        defaults.setObject(userName, "user_name")
    }

    override fun getUserName(): String {
        return defaults.stringForKey("user_name") ?: ""
    }

    override fun clear() {
        defaults.removeObjectForKey("user_name")
    }
}

actual fun createSessionManager(context: Any?): SessionManager {
    return IosSessionManager()
}
