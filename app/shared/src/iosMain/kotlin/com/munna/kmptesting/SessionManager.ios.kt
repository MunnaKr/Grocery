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

    override fun saveUserType(userType: String) {
        defaults.setObject(userType, "user_type")
    }

    override fun getUserType(): String {
        return defaults.stringForKey("user_type") ?: ""
    }

    override fun clear() {
        defaults.removeObjectForKey("user_name")
        defaults.removeObjectForKey("user_type")
    }
}

actual fun createSessionManager(context: Any?): SessionManager {
    return IosSessionManager()
}
