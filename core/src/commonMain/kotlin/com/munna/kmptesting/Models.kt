package com.munna.kmptesting

import kotlinx.serialization.Serializable

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class RegisterRequest(
    val userId: String,
    val fullName: String,
    val newPassword: String,
    val gender: String,
    val userType: String
)

@Serializable
data class AuthResponse(
    val success: Boolean,
    val message: String,
    val userName: String? = null
)

@Serializable
data class GroceryItemDto(
    val name: String,
    val icon: String,
    val type: String
)
