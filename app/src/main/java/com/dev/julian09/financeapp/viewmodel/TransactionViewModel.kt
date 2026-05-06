package com.dev.julian09.financeapp.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.julian09.financeapp.domain.model.Transaction
import com.dev.julian09.financeapp.domain.usecase.AddTransactionUseCase
import com.dev.julian09.financeapp.domain.usecase.GetTransactionsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val getTransactionsUseCase: GetTransactionsUseCase,
    private val addTransactionUseCase: AddTransactionUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    fun loadTransactions() {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            try {
                val transactions = getTransactionsUseCase()
                _uiState.value = UiState.Success(transactions)
            } catch (e: Exception) {
                _uiState.value = UiState.Error(e.message ?: "Error desconocido")
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

    sealed class UiState {
        object Loading : UiState()
        data class Success(val transactions: List<Transaction>) : UiState()
        data class Error(val message: String) : UiState()
    }
}
 