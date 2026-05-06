package com.dev.julian09.financeapp.ui.navigation

sealed class Screen(val route: String) {
    data object Main : Screen("mainScreen")
    data object Register : Screen("registerScreen")
}