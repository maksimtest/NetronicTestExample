package com.netronic.test.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.netronic.test.data.remote.UserApi
import com.netronic.test.data.repo.UserRepositoryImpl
import com.netronic.test.domain.repo.UserRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.MockWebServer
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object TestNetworkModule {
    @Provides @Singleton fun provideMockServer(): MockWebServer = MockWebServer().apply { start() }

    @Provides @Singleton fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder().build()

    @Provides @Singleton fun provideRetrofit(server: MockWebServer, client: OkHttpClient): Retrofit {
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(client)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
    }

    @Provides @Singleton fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides @Singleton
    fun provideUserRepository(api: UserApi): UserRepository =
        UserRepositoryImpl(api)
}