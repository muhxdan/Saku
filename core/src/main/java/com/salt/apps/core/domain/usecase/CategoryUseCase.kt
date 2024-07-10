package com.salt.apps.core.domain.usecase

import com.salt.apps.core.domain.model.Category
import com.salt.apps.core.domain.repository.CategoryRepository
import javax.inject.Inject

class CategoryUseCase @Inject constructor(private val categoryRepository: CategoryRepository) {
    fun getCategory(id: String) = categoryRepository.getCategory(id)

    fun getCategories() = categoryRepository.getCategories()

    suspend fun upsertCategory(category: Category) {
        categoryRepository.upsertCategory(category)
    }

    suspend fun deleteCategory(id: String) {
        categoryRepository.deleteCategory(id)
    }
}