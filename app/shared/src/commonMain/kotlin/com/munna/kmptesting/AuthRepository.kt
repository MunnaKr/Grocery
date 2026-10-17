package com.munna.kmptesting

interface AuthRepository {
    suspend fun login(request: LoginRequest): AuthResponse
    suspend fun register(request: RegisterRequest): AuthResponse
    suspend fun getGroceries(): List<GroceryItemDto>
    suspend fun uploadGroceries(items: List<GroceryItemDto>): AuthResponse
}

class AuthRepositoryImpl(private val api: ApiClient) : AuthRepository {
    override suspend fun login(request: LoginRequest): AuthResponse = api.login(request)
    override suspend fun register(request: RegisterRequest): AuthResponse = api.register(request)
    override suspend fun getGroceries(): List<GroceryItemDto> = api.getGroceries()
    override suspend fun uploadGroceries(items: List<GroceryItemDto>): AuthResponse = api.uploadGroceries(items)
}
