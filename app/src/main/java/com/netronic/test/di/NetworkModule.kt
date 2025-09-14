package com.netronic.test.di

import android.util.Log
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
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val TAG_NET = "NET.OkHttp"

    @Provides @Singleton
    fun provideOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .callTimeout(java.time.Duration.ofSeconds(15))
        .connectTimeout(java.time.Duration.ofSeconds(10))
        .readTimeout(java.time.Duration.ofSeconds(10))
        .writeTimeout(java.time.Duration.ofSeconds(10))
        .addInterceptor(
            HttpLoggingInterceptor { msg ->
                Log.d(TAG_NET, msg)
            }.setLevel(HttpLoggingInterceptor.Level.BODY)
        )
        .build()

    @Provides @Singleton
    fun provideRetrofit(client: OkHttpClient): Retrofit {
        val contentType = "application/json".toMediaType()
        val json = Json { ignoreUnknownKeys = true }
        return Retrofit.Builder()
            .baseUrl("https://randomuser.me/")
            .client(client)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Provides @Singleton
    fun provideUserApi(retrofit: Retrofit): UserApi = retrofit.create(UserApi::class.java)

    @Provides @Singleton
    fun provideUserRepository(api: UserApi): UserRepository = UserRepositoryImpl(api)
}