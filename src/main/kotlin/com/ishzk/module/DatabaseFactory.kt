package com.ishzk.module

import com.ishzk.model.ImagesTable
import com.ishzk.model.PostsTable
import com.ishzk.model.UsersTable
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction

object DatabaseFactory {
    fun init(){
        Database.connect(
            url = System.getenv("JDBC_URL"),
            driver = "org.postgresql.Driver",
            user = System.getenv("POSTGRES_USER"),
            password = System.getenv("POSTGRES_PASSWORD"),
        )

        transaction {
            SchemaUtils.apply {
                create(UsersTable)
                create(PostsTable)
                create(ImagesTable)
            }
        }
    }
}