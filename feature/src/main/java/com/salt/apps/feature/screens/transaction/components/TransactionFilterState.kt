package com.salt.apps.feature.screens.transaction.components

import com.salt.apps.core.domain.model.Category

data class TransactionFilterState(
    val selectedCategories: List<Category> = emptyList(),
    val availableCategories: List<Category> = emptyList(),
    val dateType: DateType = DateType.WEEK,
    val transactionType: TransactionType = TransactionType.INCOME,
)