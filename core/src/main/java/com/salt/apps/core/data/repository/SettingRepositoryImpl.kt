package com.salt.apps.core.data.repository

import com.salt.apps.core.datastore.PreferencesDataStore
import com.salt.apps.core.domain.model.LanguageConfig
import com.salt.apps.core.domain.model.ThemeConfig
import com.salt.apps.core.domain.model.UserData
import com.salt.apps.core.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingRepositoryImpl @Inject constructor(
    private val preferencesDataStore: PreferencesDataStore
) : SettingRepository {
    override val userData: Flow<UserData> = preferencesDataStore.userData

    override suspend fun setDarkThemeConfig(darkThemeConfig: ThemeConfig) =
        preferencesDataStore.setDarkThemeConfig(darkThemeConfig)

    override suspend fun setLanguageConfig(languageConfig: LanguageConfig) =
        preferencesDataStore.setLanguageConfig(languageConfig)
}
