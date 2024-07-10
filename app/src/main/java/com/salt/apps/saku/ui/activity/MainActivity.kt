package com.salt.apps.saku.ui.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.salt.apps.core.design.theme.SakuTheme
import com.salt.apps.core.domain.model.ThemeConfig.DARK
import com.salt.apps.core.domain.model.ThemeConfig.FOLLOW_SYSTEM
import com.salt.apps.core.domain.model.ThemeConfig.LIGHT
import com.salt.apps.saku.ui.SakuApp
import com.salt.apps.saku.ui.activity.MainActivityState.Loading
import com.salt.apps.saku.ui.activity.MainActivityState.Success
import com.salt.apps.saku.ui.rememberAppState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        askPermissionNotifications()
        var mainState: MainActivityState by mutableStateOf(Loading)
        splashScreen.setKeepOnScreenCondition {
            viewModel.mainActivityStateStateFlow.value is Loading
        }
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.mainActivityStateStateFlow.onEach { mainState = it }.collect()
            }
        }

        splashScreen.setKeepOnScreenCondition {
            when (mainState) {
                Loading -> true
                is Success -> false
            }
        }

        enableEdgeToEdge()
        setContent {
            val appState = rememberAppState()
            val darkTheme = shouldUseDarkTheme(mainState)
            CompositionLocalProvider {
                SakuTheme(
                    darkTheme = darkTheme
                ) {
                    SakuApp(appState)
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
            }
        }
    }

    private fun askPermissionNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.POST_NOTIFICATIONS), 1
                )
            }
        }
    }
}

@Composable
private fun shouldUseDarkTheme(
    uiState: MainActivityState,
): Boolean = when (uiState) {
    Loading -> isSystemInDarkTheme()
    is Success -> when (uiState.userData.darkThemeConfig) {
        FOLLOW_SYSTEM -> isSystemInDarkTheme()
        LIGHT -> false
        DARK -> true
    }
}
