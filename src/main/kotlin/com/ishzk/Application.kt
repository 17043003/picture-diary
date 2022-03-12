package com.ishzk

import com.ishzk.model.PostRequest
import com.ishzk.model.UserRequest
import com.ishzk.module.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ishzk.plugins.*
import com.ishzk.repository.PostRepository
import com.ishzk.repository.UserRepository
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*
import java.lang.IllegalArgumentException

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureSecurity()
    configureRouting(
        PostRepository(),
        UserRepository()
    )
    configureSerialization()
    configureHTTP()

    DatabaseFactory.init()

    routing {

    }
}
