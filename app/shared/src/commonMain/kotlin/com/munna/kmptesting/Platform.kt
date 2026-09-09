package com.munna.kmptesting

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform