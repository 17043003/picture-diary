package com.ishzk

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ishzk.plugins.*
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureSecurity()

    routing {
    }
}
