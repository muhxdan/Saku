package com.salt.apps.core.domain.model

data class TransactionCategory(
    val transaction: Transaction,
    val category: Category?
)

