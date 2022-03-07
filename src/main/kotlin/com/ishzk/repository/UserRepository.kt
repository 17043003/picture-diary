package com.ishzk.repository

import com.ishzk.model.UserRequest
import com.ishzk.model.UsersTable
import org.jetbrains.exposed.sql.insert
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
}