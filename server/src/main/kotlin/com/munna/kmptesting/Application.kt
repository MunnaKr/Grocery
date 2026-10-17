package com.munna.kmptesting

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

// Simple in-memory database
val userDatabase = mutableListOf<RegisterRequest>()

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    routing {
        get("/") {
            call.respondText("Welcome to KMP Server!")
        }
        post("/login") {
            val url = call.request.uri
            try {
                val requestBody = call.receiveText()
                println("SERVER REQUEST URL: $url")
                println("SERVER REQUEST BODY: $requestBody")

                val request = Json.decodeFromString<LoginRequest>(requestBody)
                val user = userDatabase.find { it.userId == request.username && it.newPassword == request.password }
                
                val response = if (user != null) {
                    AuthResponse(true, "Welcome ${user.fullName}, Login successful", user.fullName)
                } else {
                    AuthResponse(false, "Invalid User ID or password")
                }
                
                val responseJson = Json.encodeToString(response)
                println("SERVER RESPONSE: $responseJson")
                call.respond(response)
            } catch (e: Exception) {
                val errorResponse = AuthResponse(false, "Invalid request format: ${e.message}")
                println("SERVER REQUEST URL: $url")
                println("SERVER RESPONSE ERROR: ${Json.encodeToString(errorResponse)}")
                call.respond(errorResponse)
            }
        }
        post("/register") {
            val url = call.request.uri
            try {
                val requestBody = call.receiveText()
                println("SERVER REQUEST URL: $url")
                println("SERVER REQUEST BODY: $requestBody")

                val request = Json.decodeFromString<RegisterRequest>(requestBody)
                val alreadyExists = userDatabase.any { it.userId == request.userId }
                
                val response = if (alreadyExists) {
                    AuthResponse(false, "User already exists with this User ID")
                } else {
                    userDatabase.add(request)
                    AuthResponse(true, "User ${request.fullName} registered successfully")
                }
                
                val responseJson = Json.encodeToString(response)
                println("SERVER RESPONSE: $responseJson")
                call.respond(response)
            } catch (e: Exception) {
                val errorResponse = AuthResponse(false, "Invalid request format: ${e.message}")
                println("SERVER REQUEST URL: $url")
                println("SERVER RESPONSE ERROR: ${Json.encodeToString(errorResponse)}")
                call.respond(errorResponse)
            }
        }
    }
}
