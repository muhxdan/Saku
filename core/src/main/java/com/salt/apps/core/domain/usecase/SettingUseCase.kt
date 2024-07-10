package com.salt.apps.core.domain.usecase

import com.salt.apps.core.domain.model.LanguageConfig
import com.salt.apps.core.domain.model.ThemeConfig
import com.salt.apps.core.domain.model.UserData
import com.salt.apps.core.domain.repository.SettingRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingUseCase @Inject constructor(private val settingRepository: SettingRepository) {
    val userData: Flow<UserData> = settingRepository.userData

    suspend fun setDarkThemeConfig(darkThemeConfig: ThemeConfig) =
        settingRepository.setDarkThemeConfig(darkThemeConfig)

    suspend fun setLanguageConfig(languageConfig: LanguageConfig) =
        settingRepository.setLanguageConfig(languageConfig)
}