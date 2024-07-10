package com.salt.apps.core.data.source.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.salt.apps.core.data.source.local.dao.CategoryDao
import com.salt.apps.core.data.source.local.dao.SubscriptionDao
import com.salt.apps.core.data.source.local.dao.TransactionDao
import com.salt.apps.core.data.source.local.entity.CategoryEntity
import com.salt.apps.core.data.source.local.entity.SubscriptionEntity
import com.salt.apps.core.data.source.local.entity.TransactionCategoryCrossRefEntity
import com.salt.apps.core.data.source.local.entity.TransactionEntity
import kotlinx.datetime.Instant
import java.math.BigDecimal

@Database(
    entities = [CategoryEntity::class, TransactionEntity::class, TransactionCategoryCrossRefEntity::class, SubscriptionEntity::class],
    version = 3,
    exportSchema = false
)

@TypeConverters(
    InstantConverter::class,
    BigDecimalConverter::class,
)
internal abstract class SakuDatabase : RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun subscriptionDao(): SubscriptionDao
}

internal class InstantConverter {

    @TypeConverter
    fun longToInstant(value: Long?): Instant? = value?.let(Instant::fromEpochMilliseconds)

    @TypeConverter
    fun instantToLong(instant: Instant?): Long? = instant?.toEpochMilliseconds()
}

internal class BigDecimalConverter {

    @TypeConverter
    fun fromBigDecimal(value: BigDecimal?): String? = value?.toString()

    @TypeConverter
    fun toBigDecimal(value: String?): BigDecimal? = value?.let { BigDecimal(it) }
}