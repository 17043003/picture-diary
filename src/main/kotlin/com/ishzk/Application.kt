package com.ishzk

import com.ishzk.model.PostRequest
import com.ishzk.module.DatabaseFactory
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.ishzk.plugins.*
import com.ishzk.repository.PostRepository
import com.typesafe.config.ConfigFactory
import io.ktor.application.*
import io.ktor.config.*
import io.ktor.http.*
import io.ktor.request.*
import io.ktor.response.*
import io.ktor.routing.*

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module(testing: Boolean = false) {
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureSecurity()

    DatabaseFactory.init()
    val postRepository = PostRepository()

    routing {
        get("/api/post"){
            val posts = postRepository.getPosts()
            call.respond(posts)
        }

        post("/api/post"){
            val postParameters: Parameters = call.receiveParameters()
            postRepository.newPost(
                PostRequest(
                    title = postParameters["title"]?: "",
                    body = postParameters["body"] ?: "",
                    imageUrls = listOf(postParameters["imageUrl"] ?: "")
                )
            )
            call.respond("status" to "200")
        }
    }
}
