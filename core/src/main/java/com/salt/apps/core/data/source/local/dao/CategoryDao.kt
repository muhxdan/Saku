package com.salt.apps.core.data.source.local.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.salt.apps.core.data.source.local.entity.CategoryEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CategoryDao {

    @Query("SELECT * FROM category WHERE id = :id")
    fun getCategoryEntity(id: String): Flow<CategoryEntity>

    @Query("SELECT * FROM category ORDER BY id DESC")
    fun getCategoryEntities(): Flow<List<CategoryEntity>>

    @Upsert
    suspend fun upsertCategory(category: CategoryEntity)

    @Query("DELETE FROM category WHERE id = :id")
    suspend fun deleteCategory(id: String)
}