package com.ishzk.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.jodatime.datetime
import org.joda.time.DateTime
import org.joda.time.DateTimeZone

object PostsTable: IntIdTable("posts") {
    val title = varchar("title", 255).nullable()
    val body = varchar("body", 65535).nullable()
    val userId = integer("userId").references(UsersTable.id, fkName = "fk_user_id")
    val created = datetime("created").default(DateTime(1970, 1, 1,0, 0, 0, DateTimeZone.UTC))
    val updated = datetime("updated").default(DateTime(1970, 1, 1,0, 0, 0, DateTimeZone.UTC))

    fun toPost(row: ResultRow): Post {
        return Post(
            id = row[id].value.toLong(),
            title = row[title]!!,
            body = row[body]!!,
            imageUrls = listOf(row[ImagesTable.url]),
            userId = row[userId].toLong(),
            created = row[created].millis,
            updated = row[updated].millis,
        )
    }
}

data class Post(
    val id: Long,
    val title: String,
    val body: String,
    val imageUrls: List<String>? = null,
    val userId: Long,
    val created: Long,
    val updated: Long,
)

data class PostRequest(
    val title: String,
    val body: String,
    val imageUrls: List<String>? = null,
    val userId: Long,
    val created: DateTime = DateTime(1970, 1, 1,0, 0, 0, DateTimeZone.UTC),
    val updated: DateTime = DateTime(1970, 1, 1,0, 0, 0, DateTimeZone.UTC),
)
