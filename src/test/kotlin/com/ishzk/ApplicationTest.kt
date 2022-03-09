package com.ishzk

import io.ktor.routing.*
import io.ktor.http.*
import io.ktor.gson.*
import io.ktor.features.*
import io.ktor.locations.*
import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ishzk.module.DatabaseFactory
import io.ktor.application.*
import io.ktor.response.*
import io.ktor.request.*
import kotlin.test.*
import io.ktor.server.testing.*
import com.ishzk.plugins.*
import com.ishzk.repository.PostRepository
import com.ishzk.repository.UserRepository

class ApplicationTest {
    @Test
    fun testUser() {
        withTestApplication({
            configureRouting(
                postRepository = PostRepository(),
                userRepository = UserRepository()
            )
            configureSerialization()
            configureHTTP()
        }) {
            DatabaseDelete.delete()
            DatabaseFactory.init()

            handleRequest(HttpMethod.Post, "/api/user"){
                addHeader(HttpHeaders.ContentType, ContentType.Application.FormUrlEncoded.toString())
                setBody(listOf("name" to "test", "email" to "test@gmail.com").formUrlEncode())
            }.apply {
                assertEquals(HttpStatusCode.OK, response.status())
            }
        }
    }
}