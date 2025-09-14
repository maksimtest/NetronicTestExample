package com.netronic.test.domain.usecase

import com.netronic.test.domain.model.User
import com.netronic.test.domain.repo.UserRepository
import javax.inject.Inject

class GetUserByIdUseCase @Inject constructor(private val repo: UserRepository) {
    suspend operator fun invoke(id: String): User? = repo.getUserById(id)
}