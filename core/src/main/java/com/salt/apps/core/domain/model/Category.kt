package com.salt.apps.core.domain.model

import com.salt.apps.core.data.source.local.entity.CategoryEntity
import kotlinx.serialization.Serializable

@Serializable
data class Category(
    val id: String? = null,
    val title: String? = null,
    val iconId: Int? = null,
)

fun Category.asEntity() = iconId?.let {
    CategoryEntity(
        id = id.toString(),
        title = title.toString(),
        iconId = it
    )
}