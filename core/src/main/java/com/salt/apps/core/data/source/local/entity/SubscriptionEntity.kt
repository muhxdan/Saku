package com.salt.apps.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.salt.apps.core.domain.model.Subscription
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Entity(
    tableName = "subscription",
)
data class SubscriptionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val amount: BigDecimal,
    val currency: String,
    @ColumnInfo(name = "payment_date") val paymentDate: Instant,
    @ColumnInfo(name = "payment_time") val paymentTime: String,
    val isRepeat: Boolean,
)

fun SubscriptionEntity.asExternalModel() = Subscription(
    id = id,
    title = title,
    amount = amount,
    currency = currency,
    paymentDate = paymentDate,
    paymentTime = paymentTime,
    isRepeat = isRepeat
)