package com.salt.apps.feature.screens.home.components

import com.salt.apps.feature.screens.transaction.components.DateType

sealed interface HomeEvent {
    data class UpdateDateType(val dateType: DateType) : HomeEvent
    data class HideTransaction(val id: String) : HomeEvent
    data object UndoTransactionRemoval : HomeEvent
    data object ClearUndoState : HomeEvent
}
