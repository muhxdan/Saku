package com.salt.apps.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Upsert
import com.salt.apps.core.data.source.local.entity.TransactionCategoryCrossRefEntity
import com.salt.apps.core.data.source.local.entity.TransactionCategoryEntity
import com.salt.apps.core.data.source.local.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {

    @Transaction
    @Query("SELECT * FROM `transaction` WHERE id = :transactionId")
    fun getTransactionWithCategoryEntity(transactionId: String): Flow<TransactionCategoryEntity>

    @Transaction
    @Query("SELECT * FROM `transaction`  ORDER BY id DESC ")
    fun getTransactions(): Flow<List<TransactionCategoryEntity>>

    @Upsert
    suspend fun upsertTransaction(transaction: TransactionEntity)

    @Query("DELETE FROM `transaction` WHERE id = :id")
    suspend fun deleteTransaction(id: String)

    @Upsert
    suspend fun upsertTransactionCategoryCrossRef(crossRef: TransactionCategoryCrossRefEntity)

    @Query("DELETE FROM transactions_categories WHERE transaction_id = :transactionId")
    suspend fun deleteTransactionCategoryCrossRef(transactionId: String)
}