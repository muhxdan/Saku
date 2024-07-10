package com.salt.apps.core.domain.model

import com.salt.apps.core.data.source.local.entity.SubscriptionEntity
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class Subscription(
    val id: String,
    val title: String,
    val amount: BigDecimal,
    val currency: String,
    val paymentDate: Instant,
    val paymentTime: String,
    val isRepeat: Boolean,
)

fun Subscription.asEntity() = SubscriptionEntity(
    id = id,
    title = title,
    amount = amount,
    currency = currency,
    paymentDate = paymentDate,
    paymentTime = paymentTime,
    isRepeat = isRepeat
)