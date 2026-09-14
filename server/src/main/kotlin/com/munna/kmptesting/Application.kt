package com.munna.kmptesting

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

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
            val request = call.receive<LoginRequest>()
            // Find user in database
            val user = userDatabase.find { it.employeeId == request.username && it.password == request.password }
            
            if (user != null) {
                call.respond(AuthResponse(true, "Welcome ${user.fullName}, Login successful", user.fullName))
            } else {
                call.respond(AuthResponse(false, "Invalid email or password"))
            }
        }
        post("/register") {
            val request = call.receive<RegisterRequest>()
            
            // Check if user already exists (by email or employeeId)
            val alreadyExists = userDatabase.any { it.email == request.email || it.employeeId == request.employeeId }
            
            if (alreadyExists) {
                call.respond(AuthResponse(false, "User already exist with this email or employee ID"))
            } else {
                userDatabase.add(request)
                call.respond(AuthResponse(true, "User ${request.fullName} registered successfully"))
            }
        }
    }
}