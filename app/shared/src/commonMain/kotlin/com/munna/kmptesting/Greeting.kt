package com.munna.kmptesting

class Greeting {
    private val platform = getPlatform()
//munna
    fun greet(): String {
        return sayHello(platform.name)
    }
}