package com.netronic.test.domain.model

import java.time.Instant

data class User(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val country: String,
    val address: String,
    val birthDate: Instant,
    val pictureUrl: String
)