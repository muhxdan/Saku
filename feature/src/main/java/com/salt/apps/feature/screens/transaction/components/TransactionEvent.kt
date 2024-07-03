package com.salt.apps.feature.screens.transaction.components

import com.salt.apps.core.domain.model.Category
import kotlinx.datetime.Instant

sealed interface TransactionEvent {

    data class UpdateTransactionId(val id: String) : TransactionEvent

    data class UpdateAmount(val amount: String) : TransactionEvent

    data class UpdateTransactionType(val type: TransactionType) : TransactionEvent

    data class UpdateCurrency(val currency: String) : TransactionEvent

    data class UpdateDate(val date: Instant) : TransactionEvent

    data class UpdateDescription(val description: String) : TransactionEvent

    data class UpdateCategory(val category: Category) : TransactionEvent

    data object Save : TransactionEvent

    data object Delete : TransactionEvent
}