package com.dev.julian09.financeapp.domain.usecase

import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class HealthCheckUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(): Boolean {
        return repository.healthCheck()
    }
}