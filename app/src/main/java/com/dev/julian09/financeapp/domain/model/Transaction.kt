package com.dev.julian09.financeapp.domain.model

data class Transaction(
    val id: Long,
    val title: String,
    val value: Double,
    val type: Boolean,
    val description: String,
    val date: String,
    val synced: Boolean
)
