package com.dev.julian09.financeapp.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [TransactionEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun transactionDao(): TransactionDao
}