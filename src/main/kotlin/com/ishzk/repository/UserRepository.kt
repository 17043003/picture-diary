package com.ishzk.repository

import com.ishzk.model.User
import com.ishzk.model.UserRequest
import com.ishzk.model.UsersTable
import com.ishzk.model.UsersTable.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction
import org.springframework.security.crypto.bcrypt.BCrypt

class UserRepository {
    fun newUser(request: UserRequest): Int {
        val salt = BCrypt.gensalt()
        return transaction {
            UsersTable.insert {
                it[name] = request.name
                it[email] = request.email
                it[passwordDigest] = BCrypt.hashpw(request.password, salt)
            }[UsersTable.id].value
        }
    }

    fun getUser(email: String): User {
        return transaction {
            UsersTable.select {
                UsersTable.email eq email
            }.map { toUser(it) }.single()
        }
    }
}