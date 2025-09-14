package com.netronic.test.presentation.users

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.netronic.test.domain.model.User
import com.netronic.test.domain.repo.UserRepository
import com.netronic.test.domain.usecase.GetUsersUseCase
import com.netronic.test.presentation.common.UiState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test
import java.time.Instant

@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModelTest {
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun checkLoading20Users() = runTest {
        val repo = object : UserRepository {
            override suspend fun getUsers(): List<User> = List(20) { i ->
                User(
                    "id-$i",
                    "User $i",
                    "u$i@test.dev",
                    "000",
                    "UA",
                    "Street $i",
                    Instant.parse("1990-01-01T00:00:00Z"),
                    "url"
                )
            }

            override suspend fun getUserById(id: String): User? = null
        }
        val vm = UsersViewModel(getUsers = GetUsersUseCase(repo))

        vm.state.test {
            assertThat(awaitItem()).isInstanceOf(UiState.Loading::class.java)
            testDispatcher.scheduler.advanceUntilIdle()
            val success = awaitItem() as UiState.Success<List<User>>
            assertThat(success.data).hasSize(20)
            cancelAndIgnoreRemainingEvents()
        }
    }
}