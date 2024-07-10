package com.salt.apps.core.data.repository

import com.salt.apps.core.data.source.local.dao.TransactionDao
import com.salt.apps.core.data.source.local.entity.TransactionCategoryEntity
import com.salt.apps.core.data.source.local.entity.asExternalModel
import com.salt.apps.core.domain.model.Transaction
import com.salt.apps.core.domain.model.TransactionCategory
import com.salt.apps.core.domain.model.TransactionCategoryCrossRef
import com.salt.apps.core.domain.model.asEntity
import com.salt.apps.core.domain.repository.TransactionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(private val transactionDao: TransactionDao) :
    TransactionRepository {
    override fun getTransactionCategory(transactionId: String): Flow<TransactionCategory> =
        transactionDao.getTransactionWithCategoryEntity(transactionId).map { it.asExternalModel() }

    override fun getTransactions(): Flow<List<TransactionCategory>> =
        transactionDao.getTransactions().map { it.map(TransactionCategoryEntity::asExternalModel) }

    override suspend fun upsertTransaction(transaction: Transaction) =
        transactionDao.upsertTransaction(transaction.asEntity())

    override suspend fun deleteTransaction(id: String) =
        transactionDao.deleteTransaction(id)

    override suspend fun upsertTransactionCategoryCrossRef(crossRef: TransactionCategoryCrossRef) =
        transactionDao.upsertTransactionCategoryCrossRef(crossRef.asEntity())

    override suspend fun deleteTransactionCategoryCrossRef(transactionId: String) =
        transactionDao.deleteTransactionCategoryCrossRef(transactionId)
}