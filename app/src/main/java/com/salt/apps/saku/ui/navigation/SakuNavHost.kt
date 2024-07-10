package com.salt.apps.saku.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import com.salt.apps.feature.navigation.HomeRoute
import com.salt.apps.feature.navigation.categoryScreen
import com.salt.apps.feature.navigation.homeScreen
import com.salt.apps.feature.navigation.navigateToCategory
import com.salt.apps.feature.navigation.navigateToSubscriptionForm
import com.salt.apps.feature.navigation.navigateToTransaction
import com.salt.apps.feature.navigation.settingScreen
import com.salt.apps.feature.navigation.subscriptionForm
import com.salt.apps.feature.navigation.subscriptionScreen
import com.salt.apps.feature.navigation.transactionScreen
import com.salt.apps.saku.ui.AppState

@Composable
fun NavHost(
    appState: AppState,
    onShowSnackbar: suspend (String, String?) -> Boolean,
    modifier: Modifier = Modifier,
) {
    val navController = appState.navController

    NavHost(
        navController = navController,
        startDestination = HomeRoute,
        enterTransition = { slideInVertically { it / 16 } + fadeIn() },
        exitTransition = { fadeOut(tween(25)) },
        popEnterTransition = { slideInVertically { it / 16 } + fadeIn() },
        popExitTransition = { fadeOut(tween(25)) },
        modifier = modifier
    ) {
        homeScreen(onShowSnackbar = onShowSnackbar, onEditTransaction = { id ->
            navController.navigateToTransaction(id = id)
        })
        categoryScreen(onShowSnackbar = onShowSnackbar)
        subscriptionScreen(onEditSubscription = { id ->
            navController.navigateToSubscriptionForm(id = id)
        }, onShowSnackbar = onShowSnackbar)
        subscriptionForm(onDone = { navController.popBackStack() })
        settingScreen(onCategoryClick = { navController.navigateToCategory() })
        transactionScreen(
            onDoneTransaction = { navController.popBackStack() },
            onCategoryCreateClick = { navController.navigateToCategory() })
    }
}