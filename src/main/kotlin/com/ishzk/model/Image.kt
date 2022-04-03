package com.ishzk.model

import org.jetbrains.exposed.dao.id.IntIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.ResultRow

object ImagesTable: IntIdTable("images") {
    val url = varchar("url", 255)
    val postId = integer("postId").references(PostsTable.id, fkName = "fk_post_id", onDelete = ReferenceOption.CASCADE)

    fun toImage(row: ResultRow): Image {
        return Image(
            id = row[id].value.toLong(),
            url = row[url],
            postId = row[postId].toLong()
        )
    }
}

data class Image(
    val id: Long,
    val url: String,
    val postId: Long,
)

