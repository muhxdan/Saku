package com.salt.apps.core.domain.repository

import com.salt.apps.core.domain.model.Subscription
import kotlinx.coroutines.flow.Flow

interface SubscriptionRepository {
    fun getSubscription(id: String): Flow<Subscription>

    fun getSubscriptions(): Flow<List<Subscription>>

    suspend fun upsertSubscription(subscription: Subscription)

    suspend fun deleteSubscription(id: String)
}