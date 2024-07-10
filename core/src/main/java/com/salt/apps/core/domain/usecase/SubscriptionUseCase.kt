package com.salt.apps.core.domain.usecase

import com.salt.apps.core.domain.model.Subscription
import com.salt.apps.core.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SubscriptionUseCase @Inject constructor(private val subscriptionRepository: SubscriptionRepository) {
    fun getSubscription(id: String): Flow<Subscription> =
        subscriptionRepository.getSubscription(id = id)

    fun getSubscriptions(): Flow<List<Subscription>> = subscriptionRepository.getSubscriptions()

    suspend fun upsertSubscription(subscription: Subscription) =
        subscriptionRepository.upsertSubscription(subscription = subscription)

    suspend fun deleteSubscription(id: String) = subscriptionRepository.deleteSubscription(id = id)
}