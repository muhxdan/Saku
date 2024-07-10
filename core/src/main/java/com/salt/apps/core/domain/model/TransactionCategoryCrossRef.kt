package com.salt.apps.core.domain.model

import com.salt.apps.core.data.source.local.entity.TransactionCategoryCrossRefEntity

data class TransactionCategoryCrossRef(
    val transactionId: String,
    val categoryId: String
)

fun TransactionCategoryCrossRef.asEntity() = TransactionCategoryCrossRefEntity(
    transactionId = transactionId,
    categoryId = categoryId
)
