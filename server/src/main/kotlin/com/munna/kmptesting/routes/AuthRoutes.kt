package com.munna.kmptesting.routes

import com.munna.kmptesting.AuthResponse
import com.munna.kmptesting.LoginRequest
import com.munna.kmptesting.RegisterRequest
import com.munna.kmptesting.data.UserRepository
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Route.authRoutes(userRepository: UserRepository) {
    post("/login") {
        val request = call.receive<LoginRequest>()
        val user = userRepository.findUserByUsername(request.username)
        
        if (user != null && user.password == request.password) {
            call.respond(AuthResponse(
                success = true,
                message = "Welcome ${user.fullName}, Login successful",
                userName = user.fullName,
                userType = user.userType
            ))
        } else {
            call.respond(AuthResponse(false, "Invalid username or password"))
        }
    }
    
    post("/register") {
        val request = call.receive<RegisterRequest>()
        
        val alreadyExists = userRepository.userExists(request.username)
        
        if (alreadyExists) {
            call.respond(AuthResponse(false, "User already exist with this username"))
        } else {
            val success = userRepository.registerUser(request)
            if (success) {
                call.respond(AuthResponse(true, "User ${request.fullName} registered successfully"))
            } else {
                call.respond(AuthResponse(false, "Registration failed"))
            }
        }
    }
}
