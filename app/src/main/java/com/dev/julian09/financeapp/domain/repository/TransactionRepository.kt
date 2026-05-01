package com.dev.julian09.financeapp.domain.repository

import com.dev.julian09.financeapp.domain.model.Transaction


interface TransactionRepository {
    suspend fun getTransactions(): List<Transaction>
}