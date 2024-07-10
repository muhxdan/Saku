package com.salt.apps.core.data.source.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.salt.apps.core.domain.model.Category

@Entity(tableName = "category")
data class CategoryEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    @ColumnInfo(name = "icon_id", defaultValue = "0")
    val iconId: Int,
)

fun CategoryEntity.asExternalModel() = Category(
    id = id,
    title = title,
    iconId = iconId,
)