package com.dev.julian09.financeapp.domain.usecase

import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.repository.TransactionRepository
import javax.inject.Inject

class AddTransactionUseCase @Inject constructor(private val repository: TransactionRepository) {

    suspend operator fun invoke(transaction: Transaction) {
        return repository.addTransaction(transaction)
    }
}
