package com.netronic.test.data.mapper


import com.netronic.test.data.remote.dto.*
import com.google.common.truth.Truth.assertThat
import com.netronic.test.data.remote.dto.NameDto
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonPrimitive
import org.junit.Before
import org.junit.Test
import java.time.ZoneId
import java.time.format.DateTimeFormatter


class UserMappersTest {
    val postcodeString = "qwest"
    val postcodeInt = 9876
    val phone = "123-45-6789"
    val email = "karl@gm.co"
    val country = "Kasablanka"
    val birthDay = "19.01.1963"
    val picture = "Nice picture"

    // user Dto with different type of postpone(String and Int)
    lateinit var userDtoWithStringPostcode: UserDto
    lateinit var userDtoWithIntPostcode: UserDto

    lateinit var address1: String
    lateinit var address2: String
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy")
        .withZone(ZoneId.systemDefault())

    @Before
    fun prepare() {
        val nameDto = NameDto("Manager", "Potakami", "Kuasoko")
        val pictureDto = PictureDto(picture, "mediumImage", "thumbnailImage")
        val dobDto = DobDto("1963-01-19T03:43:08.246Z", 20)

        val loginDto = LoginDto("23-2343-df")


        val streetDto = StreetDto(35, "Zhovnirska")
        val locationDto1 =
            LocationDto(streetDto, "Amaretto", "S", Json.parseToJsonElement(postcodeString), country)
        val locationDto2 = locationDto1.copy(postcodeAny = JsonPrimitive(postcodeInt))

        address1 =
            "${locationDto1.street.number} ${locationDto1.street.name}, ${locationDto1.city}, ${locationDto1.state} ${locationDto1.postcodeAny}"
        address2 =
            "${locationDto2.street.number} ${locationDto2.street.name}, ${locationDto2.city}, ${locationDto2.state} ${locationDto2.postcodeAny}"

        userDtoWithStringPostcode =
            UserDto(nameDto, email, phone, locationDto1, dobDto, loginDto, pictureDto)
        userDtoWithIntPostcode =
            UserDto(nameDto, email, phone, locationDto2, dobDto, loginDto, pictureDto)
    }

    @Test
    fun checkStringPostpone() {
        val user = userDtoWithStringPostcode.toDomain()
        assertThat(user.address).contains(address1)
    }

    @Test
    fun checkIntPostpone() {
        val user = userDtoWithIntPostcode.toDomain()
        assertThat(user.address).contains(address2)
    }

    @Test
    fun checkConvertUserDto() {
        val userDto = userDtoWithStringPostcode
        val userDomain = userDto.toDomain()

        assertThat(userDomain.id).contains(userDto.login.uuid)
        assertThat(userDomain.fullName).contains("${userDto.name.first} ${userDto.name.last}")
        assertThat(userDomain.email).contains(email)
        assertThat(userDomain.phone).contains(phone)
        assertThat(userDomain.country).contains(country)
        assertThat(userDomain.address).contains(address1)
        assertThat(dateFormatter.format(userDomain.birthDate)).contains(birthDay)
        assertThat(userDomain.pictureUrl).contains(picture)
    }
}