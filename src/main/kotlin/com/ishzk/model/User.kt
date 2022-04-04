package com.ishzk.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object UsersTable: IntIdTable("users"){
    val name = varchar("name", 255)
    val email = varchar("email", 255).uniqueIndex()
    val passwordDigest = varchar("password_digest", 255)

    fun toUser(row: ResultRow): User {
        return User(
            id = row[id].value.toLong(),
            name = row[name],
            email = row[email],
            passwordDigest = row[passwordDigest],
        )
    }
}

data class User(
    val id: Long,
    val name: String,
    val email: String,
    val passwordDigest: String,
)

data class UserRequest(
    val name: String,
    val email: String,
    val password: String,
)