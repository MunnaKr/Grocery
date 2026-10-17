package com.munna.kmptesting

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class ApiClient {
    private val client = HttpClient {
        install(ContentNegotiation) {
            json(Json {
                ignoreUnknownKeys = true
                useAlternativeNames = false
            })
        }
    }

    private val BASE_URL = "https://grocery-5p3u.onrender.com"

    suspend fun login(request: LoginRequest): AuthResponse {
        val url = "$BASE_URL/login"
        val requestJson = Json.encodeToString(request)
        println("CLIENT REQUEST URL: $url")
        println("CLIENT REQUEST BODY: $requestJson")

        val response: AuthResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        println("CLIENT RESPONSE: ${Json.encodeToString(response)}")
        return response
    }

    suspend fun register(request: RegisterRequest): AuthResponse {
        val url = "$BASE_URL/register"
        val requestJson = Json.encodeToString(request)
        println("CLIENT REQUEST URL: $url")
        println("CLIENT REQUEST BODY: $requestJson")

        val response: AuthResponse = client.post(url) {
            contentType(ContentType.Application.Json)
            setBody(request)
        }.body()

        println("CLIENT RESPONSE: ${Json.encodeToString(response)}")
        return response
    }
}
