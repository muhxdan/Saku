package com.salt.apps.core.data.repository

import com.salt.apps.core.data.source.local.dao.CategoryDao
import com.salt.apps.core.data.source.local.entity.CategoryEntity
import com.salt.apps.core.data.source.local.entity.asExternalModel
import com.salt.apps.core.domain.model.Category
import com.salt.apps.core.domain.model.asEntity
import com.salt.apps.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(private val categoryDao: CategoryDao) :
    CategoryRepository {
    override fun getCategory(id: String): Flow<Category> =
        categoryDao.getCategoryEntity(id).map { it.asExternalModel() }

    override fun getCategories(): Flow<List<Category>> =
        categoryDao.getCategoryEntities().map { it.map(CategoryEntity::asExternalModel) }

    override suspend fun upsertCategory(category: Category) =
        categoryDao.upsertCategory(category.asEntity()!!)

    override suspend fun deleteCategory(id: String) =
        categoryDao.deleteCategory(id)
}