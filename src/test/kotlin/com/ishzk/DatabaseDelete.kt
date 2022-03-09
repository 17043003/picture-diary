package com.ishzk

import com.ishzk.model.ImagesTable
import com.ishzk.model.PostsTable
import com.ishzk.model.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseDelete {
    fun delete() {
        Database.connect(
            url = System.getenv("JDBC_URL"),
            driver = "org.postgresql.Driver",
            user = System.getenv("POSTGRES_USER"),
            password = System.getenv("POSTGRES_PASSWORD"),
        )

        transaction {
            SchemaUtils.apply {
                drop(ImagesTable)
                drop(PostsTable)
                drop(UsersTable)
            }

        }
    }
}