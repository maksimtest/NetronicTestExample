package com.netronic.test.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.JsonElement

@Serializable
data class UserDto(
    val name: NameDto,
    val email: String,
    val phone: String,
    val location: LocationDto,
    val dob: DobDto,
    val login: LoginDto,
    val picture: PictureDto
)

@Serializable
data class NameDto(val title: String? = null, val first: String, val last: String)
@Serializable
data class PictureDto(val large: String, val medium: String? = null, val thumbnail: String? = null)
@Serializable
data class DobDto(val date: String, val age: Int)
@Serializable
data class LoginDto(val uuid: String)

@Serializable
data class LocationDto(
    val street: StreetDto,
    val city: String,
    val state: String,
    @SerialName("postcode") val postcodeAny: JsonElement,
    val country: String
)

@Serializable
data class StreetDto(val number: Int, val name: String)