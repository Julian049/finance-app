package com.dev.julian09.financeapp.ui.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dev.julian09.financeapp.ui.theme.AppBackground
import com.dev.julian09.financeapp.ui.theme.SlateBluePrimary
import com.dev.julian09.financeapp.ui.theme.TextOnDark
import com.dev.julian09.financeapp.viewmodel.TransactionViewModel

@Composable
fun MainScreen(
    onAddNewTransaction: () -> Unit,
    viewModel: TransactionViewModel = hiltViewModel()
) {

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddNewTransaction,
            ) {
                Icon(Icons.Filled.Add, "Floating action button.")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
                .background(AppBackground),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Saldo actual", modifier = Modifier
                        .weight(1f)
                        .padding(all = 8.dp)
                )
                Button(
                    onClick = {}, modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 20.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SlateBluePrimary
                    )
                ) {
                    Text("Sincronizar", color = TextOnDark)
                }
            }
            Text(
                text = "$1000.00",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(all = 8.dp),
                fontSize = 30.sp
            )
            Spacer(modifier = Modifier.padding(bottom = 15.dp))
            Text(text = "Transacciones", modifier = Modifier.padding(all = 8.dp))

            when (val state = uiState) {
                is TransactionViewModel.UiState.Loading -> {
                    Text("Cargando transacciones...", Modifier.padding(8.dp))
                }

                is TransactionViewModel.UiState.Error -> {
                    Text(
                        "Error: ${state.message}",
                        color = androidx.compose.ui.graphics.Color.Red
                    )
                }

                is TransactionViewModel.UiState.Success -> {
                    LazyColumn {
                        items(state.transactions) { transaction ->
                            TransactionItem(
                                title = transaction.title,
                                date = transaction.date,
                                status = transaction.synced,
                                value = transaction.value
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
@Preview(showBackground = true)
fun MainScreenPreview() {
    MainScreen({})
}