package com.ishzk.plugins

import com.ishzk.model.PostRequest
import com.ishzk.model.UserRequest
import com.ishzk.repository.PostRepository
import com.ishzk.repository.UserRepository
import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.locations.*
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import java.lang.IllegalArgumentException

fun Application.configureRouting(
    postRepository: PostRepository,
    userRepository: UserRepository
) {
    install(Locations) {
    }

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
                call.respond(status = HttpStatusCode.OK, mapOf("status" to "200", "userId" to userId))
            }catch (e: IllegalArgumentException){
                call.respond(status = HttpStatusCode.BadRequest, "status" to "500")
            }catch (e: org.jetbrains.exposed.exceptions.ExposedSQLException){
                call.respond(status = HttpStatusCode.BadRequest, "status" to "500")
            }
        }
    }
}

@Location("/api/post/{id}")
class PostLocation(val id: Long)