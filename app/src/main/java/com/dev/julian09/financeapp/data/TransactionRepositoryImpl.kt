package com.dev.julian09.financeapp.data

import com.dev.julian09.financeapp.data.local.TransactionDao
import com.dev.julian09.financeapp.data.local.TransactionEntity
import com.dev.julian09.financeapp.data.remote.ApiService
import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.repository.TransactionRepository
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionRepositoryImpl(
    private val dao: TransactionDao,
    private val api: ApiService
) : TransactionRepository {


    override suspend fun getTransactions(): List<Transaction> {
        return try {
            val dtos = api.getAllTransactions()

            val entities = dtos.map {
                TransactionEntity(
                    it.id,
                    it.value,
                    it.type,
                    it.description,
                    formatDate(it.date),
                    it.synced
                )
            }

            dao.insertAll(entities)

            entities.map {
                Transaction(
                    it.localId,
                    it.value,
                    it.type,
                    it.description,
                    it.date,
                    it.synced
                )
            }

        } catch (e: Exception) {
            dao.getAllTransactions().map {
                Transaction(
                    it.localId,
                    it.value,
                    it.type,
                    it.description,
                    it.date,
                    it.synced
                )
            }
        }
    }

    private fun formatDate(raw: String): String {
        return try {
            val odt = OffsetDateTime.parse(raw)
            val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy", Locale("es"))
            odt.format(formatter)
        } catch (e: Exception) {
            raw
        }
    }

}