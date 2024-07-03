package com.salt.apps.feature.screens.transaction.components

import com.salt.apps.core.domain.model.Category
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant

data class TransactionState(
    val transactionId: String = "",
    val description: String = "",
    val amount: String = "",
    val currency: String = "IDR",
    val date: Instant = Clock.System.now(),
    val category: Category? = Category(),
    val transactionType: TransactionType = TransactionType.INCOME,
    val isLoading: Boolean = false,
)