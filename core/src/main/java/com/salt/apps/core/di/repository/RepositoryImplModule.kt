package com.salt.apps.core.di.repository

import com.salt.apps.core.data.repository.CategoryRepositoryImpl
import com.salt.apps.core.data.repository.SettingRepositoryImpl
import com.salt.apps.core.data.repository.SubscriptionRepositoryImpl
import com.salt.apps.core.data.repository.TransactionRepositoryImpl
import com.salt.apps.core.domain.repository.CategoryRepository
import com.salt.apps.core.domain.repository.SettingRepository
import com.salt.apps.core.domain.repository.SubscriptionRepository
import com.salt.apps.core.domain.repository.TransactionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryImplModule {
    @Binds
    abstract fun bindsCategoryRepositoryImpl(
        categoryRepositoryImpl: CategoryRepositoryImpl
    ): CategoryRepository

    @Binds
    abstract fun bindsTransactionRepositoryImpl(
        transactionRepositoryImpl: TransactionRepositoryImpl
    ): TransactionRepository

    @Binds
    abstract fun bindsSubscriptionRepositoryImpl(
        subscriptionRepositoryImpl: SubscriptionRepositoryImpl
    ): SubscriptionRepository

    @Binds
    abstract fun bindsSettingRepositoryImpl(
        settingRepositoryImpl: SettingRepositoryImpl
    ): SettingRepository

}