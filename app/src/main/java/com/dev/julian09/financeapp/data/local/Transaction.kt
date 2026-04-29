package com.dev.julian09.financeapp.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.OffsetDateTime
import java.util.UUID

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey(autoGenerate = true) val localId: UUID,
    val value: Double,
    val type: Boolean,
    val description: String,
    val date: OffsetDateTime
)
