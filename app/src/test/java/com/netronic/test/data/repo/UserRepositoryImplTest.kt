package com.netronic.test.data.repo


import com.google.common.truth.Truth.assertThat
import com.netronic.test.ReadTestUsersJson
import com.netronic.test.data.remote.UserApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before
import org.junit.Test
import retrofit2.Retrofit
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory


class UserRepositoryImplTest {
    private lateinit var server: MockWebServer
    private lateinit var repository: UserRepositoryImpl

    @Before
    fun setUp() {
        server = MockWebServer().apply { start() }
        val retrofit = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient())
            .addConverterFactory(Json { ignoreUnknownKeys = true }
                .asConverterFactory("application/json".toMediaType()))
            .build()
        val api = retrofit.create(UserApi::class.java)
        repository = UserRepositoryImpl(api)
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun getUsersSuccesses() = kotlinx.coroutines.test.runTest {
        val body = ReadTestUsersJson.load()
        server.enqueue(MockResponse().setResponseCode(200).setBody(body))

        val list = repository.getUsers()

        assertThat(list).hasSize(20)
        val req = server.takeRequest()
        assertThat(req.path!!).contains("/api/?results=20")
    }


    @Test
    fun getUsersThrown500() = kotlinx.coroutines.test.runTest {
        server.enqueue(MockResponse().setResponseCode(500))
        try {
            repository.getUsers()
            throw AssertionError("Expected exception on 500")
        } catch (t: Throwable) {
            // ok
        }
    }
}