package com.netronic.test.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class UsersGroupDto(
    val results: List<UserDto>
)
