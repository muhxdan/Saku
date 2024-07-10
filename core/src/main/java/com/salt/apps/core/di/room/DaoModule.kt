package com.salt.apps.core.di.room

import com.salt.apps.core.data.source.local.dao.CategoryDao
import com.salt.apps.core.data.source.local.dao.SubscriptionDao
import com.salt.apps.core.data.source.local.dao.TransactionDao
import com.salt.apps.core.data.source.local.database.SakuDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DaoModule {

    @Provides
    @Singleton
    fun provideCategoryDao(database: SakuDatabase): CategoryDao = database.categoryDao()

    @Provides
    @Singleton
    fun provideTransactionDao(database: SakuDatabase): TransactionDao = database.transactionDao()

    @Provides
    @Singleton
    fun provideSubscriptionnDao(database: SakuDatabase): SubscriptionDao =
        database.subscriptionDao()
}