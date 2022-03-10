package com.ishzk.repository

import com.ishzk.model.User
import com.ishzk.model.UserRequest
import com.ishzk.model.UsersTable
import com.ishzk.model.UsersTable.toUser
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.transactions.transaction

class UserRepository {
    fun newUser(request: UserRequest): Int {
        return transaction {
            UsersTable.insert {
                it[name] = request.name
                it[email] = request.email
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