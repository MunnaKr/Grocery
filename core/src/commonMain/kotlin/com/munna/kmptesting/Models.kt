package com.munna.kmptesting

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val fullName: String,
    val employeeId: String,
    val email: String,
    val gender: String,
    val address: String,
    val password: String
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val userName: String? = null
)
