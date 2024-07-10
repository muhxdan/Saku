package com.salt.apps.core.di.room

import android.content.Context
import androidx.room.Room
import com.salt.apps.core.data.source.local.database.SakuDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal object DatabaseModule {

    @Provides
    @Singleton
    fun provideSakuDatabase(@ApplicationContext context: Context): SakuDatabase {
        return Room.databaseBuilder(
            context, SakuDatabase::class.java, "saku-database"
        ).fallbackToDestructiveMigration().build()
    }
}