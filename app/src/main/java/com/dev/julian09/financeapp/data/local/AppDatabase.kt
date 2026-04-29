package com.dev.julian09.financeapp.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Transaction::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    
}