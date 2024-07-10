package com.salt.apps.core.data.repository

import com.salt.apps.core.data.source.local.dao.SubscriptionDao
import com.salt.apps.core.data.source.local.entity.SubscriptionEntity
import com.salt.apps.core.data.source.local.entity.asExternalModel
import com.salt.apps.core.domain.model.Subscription
import com.salt.apps.core.domain.model.asEntity
import com.salt.apps.core.domain.repository.SubscriptionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SubscriptionRepositoryImpl @Inject constructor(
    private val subscriptionDao: SubscriptionDao,
//    private val notificationAlarmScheduler: NotificationAlarmScheduler,
) : SubscriptionRepository {
    override fun getSubscription(id: String): Flow<Subscription> =
        subscriptionDao.getSubscriptionEntity(id).map { it.asExternalModel() }

    override fun getSubscriptions(): Flow<List<Subscription>> =
        subscriptionDao.getSubscriptionEntities()
            .map { it.map(SubscriptionEntity::asExternalModel) }

    override suspend fun upsertSubscription(subscription: Subscription) {
        subscriptionDao.upsertSubscription(subscription.asEntity())
//        if (subscription.reminder != null) {
//            notificationAlarmScheduler.schedule(subscription.reminder)
//        } else {
//            notificationAlarmScheduler.cancel(subscription.id.hashCode())
//        }
    }

    override suspend fun deleteSubscription(id: String) {
        subscriptionDao.deleteSubscription(id)
//        notificationAlarmScheduler.cancel(id.hashCode())
    }
}