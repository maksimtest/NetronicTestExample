package com.netronic.test.data.repo

import com.netronic.test.data.mapper.toDomain
import com.netronic.test.data.remote.UserApi
import com.netronic.test.domain.model.User
import com.netronic.test.domain.repo.UserRepository
import com.netronic.test.logging.AppLogger

class UserRepositoryImpl(private val api: UserApi) : UserRepository {

    private companion object {
        const val TAG = "Repo.User"
    }

    private var cache: List<User>? = null

    override suspend fun getUsers(): List<User> {
        cache?.let {
            AppLogger.d(TAG, "getUsers() | return cache, count=${it.size}")
            return it
        }
        AppLogger.d(TAG, "getUsers() | before api call")

        val usersDto = api.getUsers(20).results
        AppLogger.d(TAG,
            "getUsers() | usersDto: size=${usersDto.size}" + usersDto.take(1)
                .joinToString { ", userDto=$it" })

        val users = usersDto.map { it.toDomain() }
        AppLogger.d(TAG,
            "getUsers() | usersDomain=${users.size}" +
                    users.take(1).joinToString { ", userDomain=$it" })
        cache = users
        return users
    }


    override suspend fun getUserById(id: String): User? {
        val current = cache ?: run {
            AppLogger.d(TAG, "getUserById() | cache miss, loading users")
            getUsers()
        }
        val user = current.firstOrNull { it.id == id }
        AppLogger.d(TAG, "getUserById() | id=$id, hit=${user != null}")
        return user
    }
}

