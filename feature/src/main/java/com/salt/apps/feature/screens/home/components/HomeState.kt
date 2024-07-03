package com.salt.apps.feature.screens.home.components

import com.salt.apps.core.domain.model.TransactionCategory
import com.salt.apps.feature.screens.transaction.components.DateType
import java.math.BigDecimal

sealed interface HomeState {

    data object Loading : HomeState

    data class Success(
        val totalBalance: BigDecimal,
        val incomeBalance: BigDecimal,
        val expenseBalance: BigDecimal,
        val dateType: DateType,
        val shouldDisplayUndoTransaction: Boolean,
        val transactions: List<TransactionCategory>,
    ) : HomeState
}