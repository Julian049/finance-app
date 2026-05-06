package com.dev.julian09.financeapp.domain.usecase

import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class GetTotalAmountUseCase @Inject constructor(
    private val repository: TransactionRepository
) {
    suspend operator fun invoke(transactions: List<Transaction>): Double {
        return repository.getTotalAmount(transactions)
    }
}