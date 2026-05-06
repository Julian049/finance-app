package com.dev.julian09.financeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.usecase.AddTransactionUseCase
import com.dev.julian09.financeapp.domain.usecase.GetTotalAmountUseCase
import com.dev.julian09.financeapp.domain.usecase.GetTransactionsUseCase
import com.dev.julian09.financeapp.domain.usecase.HealthCheckUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.nio.DoubleBuffer

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val healthCheckUseCase: HealthCheckUseCase,
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase,
    private val getTotalAmountUseCase: GetTotalAmountUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        verifyHealth()
        loadTransactions()
    }

    fun verifyHealth() {
        viewModelScope.launch {
            val result = healthCheckUseCase()
            _uiState.update { it.copy(isApiHealthy = result) }
        }
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            try {
                val transactions = getTransactionsUseCase()
                val amount = getTotalAmountUseCase(transactions)
                _uiState.update { currentState ->
                    currentState.copy(
                        isLoading = false,
                        transactions = transactions,
                        amount = amount
                    )
                }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = e.message ?: "Error desconocido"
                    )
                }
            }
        }
    }

    fun addTransaction(title: String, value: String, description: String, date: String) {
        viewModelScope.launch {

            var finalType: Boolean
            if (value.toDouble() < 0) {
                finalType = true
            } else {
                finalType = false
            }

            val transaction = Transaction(
                0,
                title,
                value.toDouble(),
                finalType,
                description,
                date,
                false
            )
            addTransactionUseCase(transaction)
        }
    }

    data class UiState(
        val isLoading: Boolean = true,
        val transactions: List<Transaction> = emptyList(),
        val amount: Double? = 0.0,
        val errorMessage: String? = null,
        val isApiHealthy: Boolean? = null
    )
}
 