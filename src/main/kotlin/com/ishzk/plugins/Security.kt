package com.ishzk.plugins

import io.ktor.sessions.*
import io.ktor.auth.*
import io.ktor.util.*
import io.ktor.auth.jwt.*
import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import com.ishzk.model.User
import com.ishzk.model.UserRequest
import com.ishzk.repository.UserRepository
import io.ktor.application.*
import io.ktor.http.*
import io.ktor.response.*
import io.ktor.request.*
import io.ktor.routing.*
import java.util.*

fun Application.configureSecurity() {
    data class MySession(val count: Int = 0)
    install(Sessions) {
        cookie<MySession>("MY_SESSION") {
            cookie.extensions["SameSite"] = "lax"
        }
    }

    val userRepository = UserRepository()

    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()
    val audience = environment.config.property("jwt.audience").getString()
    
    authentication {
            jwt {
                realm = environment.config.property("jwt.realm").getString()
                verifier(
                    JWT
                        .require(Algorithm.HMAC256(secret))
                        .withAudience(audience)
                        .withIssuer(issuer)
                        .build()
                )
                validate { credential ->
                    if (credential.payload.audience.contains(audience)) JWTPrincipal(credential.payload) else null
                }
            }
        }

    routing {
        get("/session/increment") {
                val session = call.sessions.get<MySession>() ?: MySession()
                call.sessions.set(session.copy(count = session.count + 1))
                call.respondText("Counter is ${session.count}. Refresh to increment.")
            }

        post("/login"){
            val userParam = call.receiveParameters()
            val user: User? = try {
                userParam["email"]?.let { email -> userRepository.getUser(email) }
            }catch(e: Exception){
                null
            }

            if(user == null) {
                call.respond(
                    HttpStatusCode.Unauthorized,
                    "メールアドレスかパスワードが間違っています")
                return@post
            }
            val token = JWT.create()
                .withAudience(audience)
                .withIssuer(issuer)
                .withClaim("name", user.name)
                .withExpiresAt(Date(System.currentTimeMillis() + 60000))
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token))
        }
    }
}
