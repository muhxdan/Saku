package com.salt.apps.core.domain.repository

import com.salt.apps.core.domain.model.Category
import kotlinx.coroutines.flow.Flow

interface CategoryRepository {
    fun getCategory(id: String): Flow<Category>

    fun getCategories(): Flow<List<Category>>

    suspend fun upsertCategory(category: Category)

    suspend fun deleteCategory(id: String)
}