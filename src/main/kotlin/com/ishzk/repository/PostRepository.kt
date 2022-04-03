package com.ishzk.repository

import com.ishzk.model.ImagesTable
import com.ishzk.model.Post
import com.ishzk.model.PostRequest
import com.ishzk.model.PostsTable
import com.ishzk.model.PostsTable.toPost
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import org.joda.time.DateTime
import java.lang.IllegalArgumentException

class PostRepository {
    fun newPost(request: PostRequest): Int {
        return transaction {
            val id = PostsTable.insert {
                it[title] = request.title
                it[body] = request.body
                it[userId] = request.userId.toInt()
                it[created] = DateTime.now()
                it[updated] = DateTime.now()
            }.getOrNull(PostsTable.id) ?: throw IllegalArgumentException("failed to save post.")

            if(!request.imageUrls.isNullOrEmpty()) {
                ImagesTable.insert {
                    it[url] = request.imageUrls[0]
                    it[postId] = id.value
                }
            }
            id.value
        }
    }

    fun updatePost(request: PostRequest, id: Long){
        transaction {
            PostsTable.update({PostsTable.id eq id.toInt()}) {
                it[title] = request.title
                it[body] = request.body
                it[updated] = DateTime.now()
            }

            if(!request.imageUrls.isNullOrEmpty()) {
                ImagesTable.update ({ ImagesTable.postId eq id.toInt() }){
                    it[url] = request.imageUrls[0]
                    it[postId] = id.toInt()
                }
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

    fun deletePost(postId: Long) {
        transaction {
            PostsTable.deleteWhere { PostsTable.id eq postId.toInt() }
        }
    }
}