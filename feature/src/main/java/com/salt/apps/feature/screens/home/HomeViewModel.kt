package com.salt.apps.feature.screens.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salt.apps.core.domain.model.TransactionCategory
import com.salt.apps.core.domain.usecase.TransactionUseCase
import com.salt.apps.core.util.Formatter.getCurrentZonedDateTime
import com.salt.apps.core.util.Formatter.getZonedDateTime
import com.salt.apps.feature.screens.home.components.HomeEvent
import com.salt.apps.feature.screens.home.components.HomeState
import com.salt.apps.feature.screens.home.components.HomeState.Loading
import com.salt.apps.feature.screens.home.components.HomeState.Success
import com.salt.apps.feature.screens.transaction.components.DateType
import com.salt.apps.feature.screens.transaction.components.TransactionFilterState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.math.BigDecimal
import java.time.temporal.WeekFields
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase,
) : ViewModel() {
    private val shouldDisplayUndoTransactionState = MutableStateFlow(false)
    private val lastRemovedTransactionIdState = MutableStateFlow<String?>(null)
    private val transactionFilterState = MutableStateFlow(TransactionFilterState())

    val homeUiState: StateFlow<HomeState> = combine(
        transactionUseCase.getTransactions(),
        transactionFilterState,
        shouldDisplayUndoTransactionState,
        lastRemovedTransactionIdState,
    ) { transactions, transactionsFilter, shouldDisplayUndoCategory, lastRemovedTransactionId ->
        val sortedTransactions = transactions.sortedByDescending { it.transaction.timestamp }

        val filteredTransactions = when (transactionsFilter.dateType) {
            DateType.ALL -> sortedTransactions
            DateType.WEEK -> filterByCurrentWeek(sortedTransactions)
            DateType.MONTH -> sortedTransactions.filter { it.transaction.timestamp.getZonedDateTime().month == getCurrentZonedDateTime().month }
        }.filterNot { it.transaction.id == lastRemovedTransactionId }

        val incomeTransactions =
            filteredTransactions.filter { it.transaction.amount > BigDecimal.ZERO }
        val expenseTransactions =
            filteredTransactions.filter { it.transaction.amount < BigDecimal.ZERO }

        val totalBalance = filteredTransactions.sumOf { it.transaction.amount }
        val incomeBalance = incomeTransactions.sumOf { it.transaction.amount }
        val expenseBalance = expenseTransactions.sumOf { it.transaction.amount }
        Success(
            totalBalance = totalBalance,
            incomeBalance = incomeBalance,
            expenseBalance = expenseBalance,
            shouldDisplayUndoTransaction = shouldDisplayUndoCategory,
            dateType = transactionsFilter.dateType,
            transactions = filteredTransactions,
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = Loading,
    )

    private fun filterByCurrentWeek(transactions: List<TransactionCategory>): List<TransactionCategory> {
        val currentWeek = getCurrentZonedDateTime().get(WeekFields.ISO.weekOfWeekBasedYear())
        return transactions.filter {
            it.transaction.timestamp.getZonedDateTime()
                .get(WeekFields.ISO.weekOfWeekBasedYear()) == currentWeek
        }
    }

    // Transactions
    private fun updateDateType(dateType: DateType) {
        transactionFilterState.update {
            it.copy(dateType = dateType)
        }
    }

    private fun deleteTransaction(id: String) {
        viewModelScope.launch {
            transactionUseCase.deleteTransaction(id)
        }
    }

    private fun undoTransactionRemoval() {
        lastRemovedTransactionIdState.value = null
        shouldDisplayUndoTransactionState.value = false
    }

    private fun clearUndoState() {
        lastRemovedTransactionIdState.value?.let(::deleteTransaction)
        undoTransactionRemoval()
    }

    private fun hideTransaction(id: String) {
        if (lastRemovedTransactionIdState.value != null) {
            clearUndoState()
        }
        shouldDisplayUndoTransactionState.value = true
        lastRemovedTransactionIdState.value = id
    }

    fun onHomeEvent(event: HomeEvent) {
        when (event) {
            is HomeEvent.HideTransaction -> hideTransaction(event.id)
            is HomeEvent.UndoTransactionRemoval -> undoTransactionRemoval()
            is HomeEvent.ClearUndoState -> clearUndoState()
            is HomeEvent.UpdateDateType -> updateDateType(event.dateType)
        }
    }
}

