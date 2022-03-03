package com.ishzk.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow

object PostsTable: IntIdTable("posts") {
    val title = varchar("title", 255).nullable()
    val body = varchar("body", 65535).nullable()

    fun toPost(row: ResultRow): Post {
        return Post(
            id = row[id].value.toLong(),
            title = row[title]!!,
            body = row[body]!!
        )
    }
}

data class Post(
    val id: Long,
    val title: String,
    val body: String,
    val imageUrls: List<String>? = null
)

data class PostRequest(
    val title: String,
    val body: String,
    val imageUrls: List<String>? = null
)
