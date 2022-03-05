package com.ishzk.repository

import com.ishzk.model.ImagesTable
import com.ishzk.model.Post
import com.ishzk.model.PostRequest
import com.ishzk.model.PostsTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction
import java.lang.IllegalArgumentException

class PostRepository {
    fun newPost(request: PostRequest) {
        transaction {
            val id = PostsTable.insert {
                it[title] = request.title
                it[body] = request.body
            }.getOrNull(PostsTable.id) ?: throw IllegalArgumentException("failed to save post.")

            if(request.imageUrls.isNullOrEmpty()) return@transaction
            ImagesTable.insert {
                it[url] = request.imageUrls[0]
                it[postId] = id.value
            }
        }
    }

    fun getPosts(): List<Post> {
        return transaction {
            PostsTable.selectAll().map { PostsTable.toPost(it) }
        }
    }
}