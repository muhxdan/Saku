package com.salt.apps.core.data.source.local.entity

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.salt.apps.core.domain.model.TransactionCategory

data class TransactionCategoryEntity(
    @Embedded
    val transaction: TransactionEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "id",
        associateBy = Junction(
            value = TransactionCategoryCrossRefEntity::class,
            parentColumn = "transaction_id",
            entityColumn = "category_id",
        )
    )
    val category: CategoryEntity?
)

fun TransactionCategoryEntity.asExternalModel() = TransactionCategory(
    transaction = transaction.asExternalModel(),
    category = category?.asExternalModel(),
)