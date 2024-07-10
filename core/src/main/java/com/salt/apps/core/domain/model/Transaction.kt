package com.salt.apps.core.domain.model

import com.salt.apps.core.data.source.local.entity.TransactionEntity
import kotlinx.datetime.Instant
import java.math.BigDecimal

data class Transaction(
    val id: String,
    val description: String?,
    val amount: BigDecimal,
    val timestamp: Instant,
)

fun Transaction.asEntity() = TransactionEntity(
    id = id,
    amount = amount,
    timestamp = timestamp,
    description = description,
)