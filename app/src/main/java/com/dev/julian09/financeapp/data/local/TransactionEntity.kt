package com.dev.julian09.financeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val localId: Long = 0,
    val value: Double,
    val type: Boolean,
    val description: String,
    val date: String,
    val synced: Boolean = false
)
