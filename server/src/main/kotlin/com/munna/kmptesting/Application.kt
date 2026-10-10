package com.munna.kmptesting

import com.munna.kmptesting.data.UserRepository
import com.munna.kmptesting.routes.authRoutes
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.http.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun main() {
    val port = System.getenv("PORT")?.toInt() ?: 8080
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
    val port = System.getenv("PORT")?.toInt() ?: 8080
    DatabaseFactory.init()
    embeddedServer(Netty, port = port, host = "0.0.0.0", module = Application::module)
        .start(wait = true)
}

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }

    install(StatusPages) {
        exception<Throwable> { call, cause ->
            call.respond(
                HttpStatusCode.InternalServerError,
                AuthResponse(false, "Server Error: ${cause.message}")
            )
        }
    }

    val userRepository = UserRepository()

    routing {
        get("/") {
            call.respondText("Welcome to KMP Server!")
        }
        authRoutes(userRepository)
    }
}
