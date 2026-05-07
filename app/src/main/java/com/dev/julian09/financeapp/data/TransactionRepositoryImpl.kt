package com.dev.julian09.financeapp.data

import android.util.Log
import com.dev.julian09.financeapp.data.local.TransactionDao
import com.dev.julian09.financeapp.data.local.TransactionEntity
import com.dev.julian09.financeapp.data.remote.ApiService
import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

class TransactionRepositoryImpl(
    private val dao: TransactionDao,
    private val api: ApiService
) : TransactionRepository {

    override suspend fun healthCheck(): Boolean {
        return try {
            val response = api.healthCheck()
            response.code() == 200
        } catch (e: Exception) {
            Log.e("REPOSITORIOLOG", "Error en el health check: ${e.message}")
            false
        }
    }

    override fun getTransactions(): Flow<List<Transaction>> {
        return dao.getAllTransactions().map { entities ->
            entities.map { entity ->
                Transaction(
                    entity.localId,
                    entity.title,
                    entity.value,
                    entity.type,
                    entity.description,
                    entity.date,
                    entity.synced
                )
            }
        }
    }

    override suspend fun syncTransactions() {
        try {
            Log.d("REPOSITORIOLOG", "Iniciando sincronización...")
            val dtos = api.getAllTransactions()

            val entities = dtos.map { dto ->
                TransactionEntity(
                    dto.id,
                    dto.title,
                    dto.value,
                    dto.type,
                    dto.description,
                    formatDate(dto.date),
                    dto.synced
                )
            }


            dao.insertAll(entities)
            Log.d("REPOSITORIOLOG", "Sincronización completada: ${dtos.size} elementos.")

        } catch (e: Exception) {
            Log.e("REPOSITORIOLOG", "Error al sincronizar: ${e.message}")
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

    override suspend fun getTotalAmount(transactions: List<Transaction>): Double {
        var total = 0.0
        Log.d("TOTALAMOUNT", "Inicia a calcular el saldo $total")
        transactions.forEach { transaction ->
            total += transaction.value
            Log.d("TOTALAMOUNT", "Saldo: $total")
        }

        return total
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