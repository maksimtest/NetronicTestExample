package com.netronic.test.domain.repo

import com.netronic.test.domain.model.User

interface UserRepository {
    suspend fun getUsers(): List<User>
    suspend fun getUserById(id: String): User?
}