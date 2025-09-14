package com.netronic.test.data.mapper

import com.netronic.test.data.remote.dto.UserDto
import com.netronic.test.domain.model.User
import kotlinx.serialization.json.JsonPrimitive
import java.time.Instant

fun UserDto.toDomain(): User {

    val postcode = when (val p = location.postcodeAny) {
        is JsonPrimitive -> p.content
        else -> ""
    }
    val address =
        "${location.street.number} ${location.street.name}, ${location.city}, ${location.state} $postcode"
    val user = User(
        id = login.uuid,
        fullName = "${name.first} ${name.last}",
        email = email,
        phone = phone,
        country = location.country,
        address = address,
        birthDate = Instant.parse(dob.date),
        pictureUrl = picture.large
    )
    return user;
}