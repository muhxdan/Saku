package com.salt.apps.feature.screens.transaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.salt.apps.core.domain.model.Category
import com.salt.apps.core.domain.model.Transaction
import com.salt.apps.core.domain.model.TransactionCategoryCrossRef
import com.salt.apps.core.domain.usecase.TransactionUseCase
import com.salt.apps.feature.screens.transaction.components.TransactionEvent
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.Delete
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.Save
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateAmount
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateCategory
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateCurrency
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateDate
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateDescription
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateTransactionId
import com.salt.apps.feature.screens.transaction.components.TransactionEvent.UpdateTransactionType
import com.salt.apps.feature.screens.transaction.components.TransactionState
import com.salt.apps.feature.screens.transaction.components.TransactionType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.Instant
import kotlinx.datetime.toKotlinInstant
import java.math.BigDecimal
import java.time.ZonedDateTime
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class TransactionViewModel @Inject constructor(
    private val transactionUseCase: TransactionUseCase,
) : ViewModel() {

    private val _transactionState = MutableStateFlow(TransactionState())
    val transactionState = _transactionState.asStateFlow()

    fun onTransactionEvent(event: TransactionEvent) {
        when (event) {
            Save -> saveTransaction()
            Delete -> deleteTransaction()
            is UpdateTransactionId -> updateTransactionId(event.id)
            is UpdateCurrency -> updateCurrency(event.currency)
            is UpdateDate -> updateDate(event.date)
            is UpdateAmount -> updateAmount(event.amount)
            is UpdateTransactionType -> updateTransactionType(event.type)
            is UpdateCategory -> updateCategory(event.category)
            is UpdateDescription -> updateDescription(event.description)
        }
    }

    private fun saveTransaction() {
        val transaction = Transaction(
            id = _transactionState.value.transactionId.ifEmpty {
                UUID.randomUUID().toString()
            },
            description = _transactionState.value.description,
            amount = when (_transactionState.value.transactionType) {
                TransactionType.EXPENSE -> _transactionState.value.amount.toBigDecimal().let {
                    if (it > BigDecimal.ZERO) it.negate() else it
                }

                TransactionType.INCOME -> _transactionState.value.amount.toBigDecimal().abs()
            },
            timestamp = ZonedDateTime.now().toInstant().toKotlinInstant(),
        )
        val transactionCategoryCrossRef = _transactionState.value.category?.id?.let { categoryId ->
            TransactionCategoryCrossRef(
                transactionId = transaction.id,
                categoryId = categoryId,
            )
        }
        viewModelScope.launch {
            transactionUseCase.upsertTransaction(transaction)
            transactionUseCase.deleteTransactionCategoryCrossRef(transaction.id)
            if (transactionCategoryCrossRef != null) {
                transactionUseCase.upsertTransactionCategoryCrossRef(transactionCategoryCrossRef)
            }
        }
        _transactionState.value = TransactionState()
    }

    private fun deleteTransaction() {
        viewModelScope.launch {
            transactionUseCase.deleteTransaction(_transactionState.value.transactionId)
        }
    }

    private fun updateTransactionId(id: String) {
        _transactionState.update {
            it.copy(transactionId = id)
        }
        if (_transactionState.value.transactionId.isEmpty()) {
            _transactionState.update {
                TransactionState()
            }
        } else {
            loadTransaction()
        }
    }

    private fun updateCurrency(currency: String) {
        _transactionState.update {
            it.copy(currency = currency)
        }
    }

    private fun updateDate(date: Instant) {
        _transactionState.update {
            it.copy(date = date)
        }
    }

    private fun updateAmount(amount: String) {
        _transactionState.update {
            it.copy(amount = amount)
        }
    }

    private fun updateTransactionType(type: TransactionType) {
        _transactionState.update {
            it.copy(transactionType = type)
        }
    }

    private fun updateCategory(category: Category) {
        _transactionState.update {
            it.copy(category = category)
        }
    }

    private fun updateDescription(description: String) {
        _transactionState.update {
            it.copy(description = description)
        }
    }

    private fun loadTransaction() {
        viewModelScope.launch {
            transactionUseCase.getTransactionCategory(_transactionState.value.transactionId)
                .onStart { _transactionState.update { it.copy(isLoading = true) } }
                .onCompletion { _transactionState.update { it.copy(isLoading = false) } }
                .catch { _transactionState.value = TransactionState() }.collect {
                    _transactionState.value = TransactionState(
                        transactionId = it.transaction.id,
                        description = it.transaction.description.toString(),
                        amount = it.transaction.amount.abs().toString(),
                        transactionType = if (it.transaction.amount < BigDecimal.ZERO) TransactionType.EXPENSE else TransactionType.INCOME,
                        date = it.transaction.timestamp,
                        category = it.category,
                    )
                }
        }
    }
}