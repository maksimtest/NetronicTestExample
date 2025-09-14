package com.netronic.test.domain.usecase

import com.google.common.truth.Truth.assertThat
import com.netronic.test.domain.model.User
import com.netronic.test.domain.repo.UserRepository
import kotlinx.coroutines.test.runTest
import org.junit.Test
import java.time.Instant

class UseCasesTest {
    private class TestRepository : UserRepository {
        private val list = List(20) { i ->
            User(
                id = "id-$i",
                fullName = "User $i",
                email = "u$i@test.dev",
                phone = "000",
                country = "UA",
                address = "Street $i",
                birthDate = Instant.parse("1990-01-01T00:00:00Z"),
                pictureUrl = "https://example.com/$i.jpg"
            )
        }

        override suspend fun getUsers() = list
        override suspend fun getUserById(id: String) = list.firstOrNull { it.id == id }
    }

    @Test
    fun getUsersUseCase() = runTest {
        val repository = TestRepository()
        val useCase = GetUsersUseCase(repository)
        assertThat(useCase().size).isEqualTo(20)
    }

    @Test
    fun getUserByIdUseCase() = runTest {
        val repository = TestRepository()
        val useCase = GetUserByIdUseCase(repository)
        val user = useCase("id-5")
        assertThat(user!!.fullName).isEqualTo("User 5")
    }
}