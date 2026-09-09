package com.munna.kmptesting

interface AuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: RegisterRequest): AuthResponse
}

class AuthRepositoryImpl(private val api: ApiClient) : AuthRepository {
    override suspend fun login(request: LoginRequest): AuthResponse = api.login(request)
    override suspend fun register(request: RegisterRequest): AuthResponse = api.register(request)
}
