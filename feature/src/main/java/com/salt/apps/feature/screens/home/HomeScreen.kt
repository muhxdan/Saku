package com.salt.apps.feature.screens.home

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.salt.apps.feature.screens.home.components.composable.HomeContent
import com.salt.apps.feature.screens.transaction.TransactionViewModel

@Composable
fun HomeScreen(
    onShowSnackbar: suspend (String, String?) -> Boolean,
    homeViewModel: HomeViewModel = hiltViewModel(),
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    onEditTransaction: (String) -> Unit
) {

    val transactionState by homeViewModel.homeUiState.collectAsStateWithLifecycle()
    HomeContent(
        homeState = transactionState,
        onShowSnackbar = onShowSnackbar,
        onHomeEvent = homeViewModel::onHomeEvent,
        onEditTransaction = onEditTransaction,
        onTransactionEvent = transactionViewModel::onTransactionEvent,
    )
}
