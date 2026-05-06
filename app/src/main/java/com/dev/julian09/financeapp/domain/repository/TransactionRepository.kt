package com.dev.julian09.financeapp.domain.repository

import com.dev.julian09.financeapp.domain.model.Transaction


interface TransactionRepository {
    suspend fun healthCheck(): Boolean
    suspend fun getTransactions(): List<Transaction>
    suspend fun addTransaction(transaction: Transaction)
    suspend fun getTotalAmount(transactions: List<Transaction>): Double
}