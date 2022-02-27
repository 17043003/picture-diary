package com.ishzk.model

import org.jetbrains.exposed.dao.id.IntIdTable

object PostsTable: IntIdTable("posts") {
    val title = varchar("title", 255).nullable()
    val body = varchar("body", 65535).nullable()
}

data class Post(
    val id: Long,
    val title: String,
    val body: String
)

data class PostRequest(
    val title: String,
    val body: String
)