package com.salt.apps.core.domain.repository

import com.salt.apps.core.domain.model.Transaction
import com.salt.apps.core.domain.model.TransactionCategory
import com.salt.apps.core.domain.model.TransactionCategoryCrossRef
import kotlinx.coroutines.flow.Flow

interface TransactionRepository {

    fun getTransactionCategory(transactionId: String): Flow<TransactionCategory>

    fun getTransactions(): Flow<List<TransactionCategory>>

    suspend fun upsertTransaction(transaction: Transaction)

    suspend fun deleteTransaction(id: String)

    suspend fun upsertTransactionCategoryCrossRef(crossRef: TransactionCategoryCrossRef)

    suspend fun deleteTransactionCategoryCrossRef(transactionId: String)
}