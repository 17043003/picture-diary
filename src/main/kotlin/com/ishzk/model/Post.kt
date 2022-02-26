package com.ishzk.model

import org.jetbrains.exposed.dao.id.IntIdTable

object PostsTable: IntIdTable("posts") {
    val title = varchar("title", 255).nullable()
    val body = varchar("body", 1023).nullable()
}