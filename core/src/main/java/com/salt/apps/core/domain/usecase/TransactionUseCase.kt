package com.salt.apps.core.domain.usecase

import com.salt.apps.core.domain.model.Transaction
import com.salt.apps.core.domain.model.TransactionCategoryCrossRef
import com.salt.apps.core.domain.repository.TransactionRepository
import javax.inject.Inject

class TransactionUseCase @Inject constructor(private val transactionsRepository: TransactionRepository) {
    fun getTransactionCategory(transactionId: String) =
        transactionsRepository.getTransactionCategory(transactionId)

    suspend fun upsertTransaction(transaction: Transaction) =
        transactionsRepository.upsertTransaction(transaction)

    fun getTransactions() = transactionsRepository.getTransactions()

    suspend fun deleteTransaction(id: String) = transactionsRepository.deleteTransaction(id)

    suspend fun upsertTransactionCategoryCrossRef(crossRef: TransactionCategoryCrossRef) =
        transactionsRepository.upsertTransactionCategoryCrossRef(crossRef)

    suspend fun deleteTransactionCategoryCrossRef(transactionId: String) =
        transactionsRepository.deleteTransactionCategoryCrossRef(transactionId)
}