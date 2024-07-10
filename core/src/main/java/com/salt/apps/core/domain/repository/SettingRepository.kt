package com.salt.apps.core.domain.repository

import com.salt.apps.core.domain.model.LanguageConfig
import com.salt.apps.core.domain.model.ThemeConfig
import com.salt.apps.core.domain.model.UserData
import kotlinx.coroutines.flow.Flow

interface SettingRepository {

    val userData: Flow<UserData>

    suspend fun setDarkThemeConfig(darkThemeConfig: ThemeConfig)

    suspend fun setLanguageConfig(languageConfig: LanguageConfig)

}