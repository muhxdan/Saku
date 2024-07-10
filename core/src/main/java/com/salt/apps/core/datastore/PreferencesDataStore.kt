package com.salt.apps.core.datastore

import androidx.datastore.core.DataStore
import com.salt.apps.core.domain.model.LanguageConfig
import com.salt.apps.core.domain.model.LanguageConfig.EN
import com.salt.apps.core.domain.model.LanguageConfig.ID
import com.salt.apps.core.domain.model.ThemeConfig
import com.salt.apps.core.domain.model.ThemeConfig.DARK
import com.salt.apps.core.domain.model.ThemeConfig.FOLLOW_SYSTEM
import com.salt.apps.core.domain.model.ThemeConfig.LIGHT
import com.salt.apps.core.domain.model.UserData
import kotlinx.coroutines.flow.map
import javax.inject.Inject


class PreferencesDataStore @Inject constructor(
    private val userPreferences: DataStore<UserPreferences>,
) {
    val userData = userPreferences.data.map {
        UserData(
            darkThemeConfig = when (it.darkThemeConfig) {
                DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT -> LIGHT
                DarkThemeConfigProto.DARK_THEME_CONFIG_DARK -> DARK
                null,
                DarkThemeConfigProto.DARK_THEME_CONFIG_UNSPECIFIED,
                DarkThemeConfigProto.UNRECOGNIZED,
                DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM,
                -> FOLLOW_SYSTEM
            }, languageConfig = when (it.languageConfig) {
                LanguageConfigProto.LANGUAGE_CONFIG_ID -> ID
                LanguageConfigProto.LANGUAGE_CONFIG_EN -> EN
                null, LanguageConfigProto.UNRECOGNIZED, LanguageConfigProto.LANGUAGE_CONFIG_UNSPECIFIED -> EN
            }
        )
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: ThemeConfig) {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setDarkThemeConfig(
                when (darkThemeConfig) {
                    FOLLOW_SYSTEM -> DarkThemeConfigProto.DARK_THEME_CONFIG_FOLLOW_SYSTEM
                    LIGHT -> DarkThemeConfigProto.DARK_THEME_CONFIG_LIGHT
                    DARK -> DarkThemeConfigProto.DARK_THEME_CONFIG_DARK
                }
            ).build()
        }
    }

    suspend fun setLanguageConfig(languageConfig: LanguageConfig) {
        userPreferences.updateData { preferences ->
            preferences.toBuilder().setLanguageConfig(
                when (languageConfig) {
                    ID -> LanguageConfigProto.LANGUAGE_CONFIG_ID
                    EN -> LanguageConfigProto.LANGUAGE_CONFIG_EN
                }
            ).build()
        }
    }
}