package com.ishzk.repository

import com.ishzk.model.ImagesTable
import com.ishzk.model.Post
import com.ishzk.model.PostRequest
import com.ishzk.model.PostsTable
import com.ishzk.model.PostsTable.toPost
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import org.jetbrains.exposed.sql.update
import java.lang.IllegalArgumentException

class PostRepository {
    fun newPost(request: PostRequest) {
        transaction {
            val id = PostsTable.insert {
                it[title] = request.title
                it[body] = request.body
                it[userId] = request.userId.toInt()
                it[created] = request.created
                it[updated] = request.updated
            }.getOrNull(PostsTable.id) ?: throw IllegalArgumentException("failed to save post.")

            if(request.imageUrls.isNullOrEmpty()) return@transaction
            ImagesTable.insert {
                it[url] = request.imageUrls[0]
                it[postId] = id.value
            }
        }
    }

    fun updatePost(request: PostRequest, id: Long): Int{
        return transaction {
            PostsTable.update({PostsTable.id eq id.toInt()}) {
                it[title] = request.title
                it[body] = request.body
                it[updated] = request.updated
            }
        }
    }

    fun getPosts(): List<Post> {
        return transaction {
            PostsTable.leftJoin(otherTable = ImagesTable)
                .selectAll().map { toPost(it) }
        }
    }

    fun getPost(postId: Long): Post {
        return transaction {
            PostsTable.leftJoin(otherTable = ImagesTable)
                .select {
                PostsTable.id eq postId.toInt()
            }.map { toPost(it) }.single()
        }
    }
}