package com.salt.apps.core.data.source.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.salt.apps.core.domain.model.Transaction
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Entity(
    tableName = "transaction",
)
data class TransactionEntity(
    @PrimaryKey
    val id: String,
    val description: String?,
    val amount: BigDecimal,
    val timestamp: Instant,
)

fun TransactionEntity.asExternalModel() = Transaction(
    id = id,
    description = description,
    amount = amount,
    timestamp = timestamp,
)