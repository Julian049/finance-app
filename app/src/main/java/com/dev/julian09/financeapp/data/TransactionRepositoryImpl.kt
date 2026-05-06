package com.dev.julian09.financeapp.data

import android.util.Log
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
            Log.d("REPOSITORIOLOG", "1. Iniciando sincronización en el Repositorio...")
            val dtos = api.getAllTransactions()
            Log.d("REPOSITORIOLOG", "Aca segun hay ${dtos.size}")
            Log.d("REPOSITORIOLOG", "2. ¡Éxito! La API devolvió ${dtos[0]} elementos.")

            val entities = dtos.map {
                TransactionEntity(
                    it.id,
                    it.title,
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
                    it.title,
                    it.value,
                    it.type,
                    it.description,
                    it.date,
                    it.synced
                )
            }

        } catch (e: Exception) {
            Log.e("REPOSITORIOLOG", "Error al sincronizar: ${e.message}")
            dao.getAllTransactions().map {
                Transaction(
                    it.localId,
                    it.title,
                    it.value,
                    it.type,
                    it.description,
                    it.date,
                    it.synced
                )
            }
        }
    }

    override suspend fun addTransaction(transaction: Transaction) {
        val entity = TransactionEntity(
            transaction.id,
            transaction.title,
            transaction.value,
            transaction.type,
            transaction.description,
            transaction.date,
            transaction.synced
        )
        dao.insert(entity)
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