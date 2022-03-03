package com.ishzk.repository

import com.ishzk.model.Post
import com.ishzk.model.PostRequest
import com.ishzk.model.PostsTable
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction

class PostRepository {
    fun newPost(request: PostRequest) {
        transaction {
            PostsTable.insert {
                it[title] = request.title
                it[body] = request.body
            }
        }
    }

    fun getPosts(): List<Post> {
        return transaction {
            PostsTable.selectAll().map { PostsTable.toPost(it) }
        }
    }
}