package com.netronic.test.data.remote

import com.netronic.test.data.remote.dto.UsersGroupDto
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApi {
    @GET("api/")
    suspend fun getUsers(@Query("results") count: Int = 20): UsersGroupDto
}