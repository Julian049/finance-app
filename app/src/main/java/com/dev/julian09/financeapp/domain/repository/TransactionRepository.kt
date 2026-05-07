package com.dev.julian09.financeapp.domain.repository

import com.dev.julian09.financeapp.domain.model.Transaction
import kotlinx.coroutines.flow.Flow


interface TransactionRepository {
    suspend fun healthCheck(): Boolean
    fun getTransactions(): Flow<List<Transaction>>
    suspend fun syncTransactions()
    suspend fun addTransaction(transaction: Transaction)
    suspend fun getTotalAmount(transactions: List<Transaction>): Double
}