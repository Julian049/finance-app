package com.dev.julian09.financeapp.data.remote

data class TransactionDto(
    val id: Long,
    val title: String,
    val value: Double,
    val type: Boolean,
    val description: String,
    val date: String,
    val synced: Boolean
)