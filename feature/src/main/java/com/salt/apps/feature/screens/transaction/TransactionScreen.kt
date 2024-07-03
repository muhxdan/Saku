package com.salt.apps.feature.screens.transaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.salt.apps.feature.screens.category.CategoryViewModel
import com.salt.apps.feature.screens.transaction.components.composable.TransactionContent

@Composable
fun TransactionScreen(
    id: String,
    onDoneTransaction: () -> Unit,
    transactionViewModel: TransactionViewModel = hiltViewModel(),
    categoriesViewModel: CategoryViewModel = hiltViewModel(),
) {
    val transactionState by transactionViewModel.transactionState.collectAsStateWithLifecycle()
    val categoriesState by categoriesViewModel.categoriesUiState.collectAsStateWithLifecycle()

    TransactionContent(
        transactionState = transactionState,
        categoryState = categoriesState,
        onDoneTransaction = onDoneTransaction,
        onTransactionEvent = transactionViewModel::onTransactionEvent,
        id = id,
    )

}