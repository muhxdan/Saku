package com.salt.apps.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.salt.apps.core.data.source.local.entity.SubscriptionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SubscriptionDao {

    @Query("SELECT * FROM subscription WHERE id = :id")
    fun getSubscriptionEntity(id: String): Flow<SubscriptionEntity>

    @Query("SELECT * FROM subscription ORDER BY payment_date DESC")
    fun getSubscriptionEntities(): Flow<List<SubscriptionEntity>>

    @Upsert
    suspend fun upsertSubscription(subscription: SubscriptionEntity)

    @Query("DELETE FROM subscription WHERE id = :id")
    suspend fun deleteSubscription(id: String)
}