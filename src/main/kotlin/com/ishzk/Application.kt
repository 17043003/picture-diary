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
    configureRouting()
    configureSerialization()
    configureHTTP()
    configureSecurity()

    DatabaseFactory.init()
    val postRepository = PostRepository()
    val userRepository = UserRepository()

    routing {
        get("/api/post"){
            val posts = postRepository.getPosts()
            call.respond(posts)
        }

        get<PostLocation> {
            val post = postRepository.getPost(it.id)
            call.respond(post)
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

        post("/api/user"){
            val userParameters: Parameters = call.receiveParameters()
            try {
                val userId = userRepository.newUser(
                    UserRequest(
                        name = userParameters["name"] ?: "",
                        email = userParameters["email"] ?: ""
                    )
                )
                call.respond(mapOf("status" to "200", "userId" to userId))
            }catch (e: IllegalArgumentException){
                call.respond("status" to "500")
            }catch (e: org.jetbrains.exposed.exceptions.ExposedSQLException){
                call.respond("status" to "500")
            }
        }
    }
}

@Location("/api/post/{id}")
class PostLocation(val id: Long)