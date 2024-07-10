package com.salt.apps.saku.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Stable
import androidx.compose.runtime.remember
import androidx.compose.ui.util.trace
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.salt.apps.feature.navigation.CategoryRoute
import com.salt.apps.feature.navigation.HomeRoute
import com.salt.apps.feature.navigation.SettingRoute
import com.salt.apps.feature.navigation.SubscriptionFormRoute
import com.salt.apps.feature.navigation.SubscriptionRoute
import com.salt.apps.feature.navigation.TransactionRoute
import com.salt.apps.feature.navigation.navigateToHome
import com.salt.apps.feature.navigation.navigateToSetting
import com.salt.apps.feature.navigation.navigateToSubscription
import com.salt.apps.saku.ui.navigation.Destination
import com.salt.apps.saku.ui.navigation.Destination.CATEGORY
import com.salt.apps.saku.ui.navigation.Destination.HOME
import com.salt.apps.saku.ui.navigation.Destination.SETTING
import com.salt.apps.saku.ui.navigation.Destination.SUBSCRIPTION
import com.salt.apps.saku.ui.navigation.Destination.SUBSCRIPTION_FORM
import com.salt.apps.saku.ui.navigation.Destination.TRANSACTION_FORM

@Composable
fun rememberAppState(
    navController: NavHostController = rememberNavController(),
): AppState {

    return remember(
        navController,
    ) {
        AppState(
            navController = navController,
        )
    }
}

@Stable
class AppState(
    val navController: NavHostController,
) {
    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    val destination: Destination?
        @Composable get() {
            with(currentDestination) {
                if (this?.hasRoute<HomeRoute>() == true) return HOME
                if (this?.hasRoute<SubscriptionRoute>() == true) return SUBSCRIPTION
                if (this?.hasRoute<SettingRoute>() == true) return SETTING
                if (this?.hasRoute<TransactionRoute>() == true) return TRANSACTION_FORM
                if (this?.hasRoute<SubscriptionFormRoute>() == true) return SUBSCRIPTION_FORM
                if (this?.hasRoute<CategoryRoute>() == true) return CATEGORY
            }
            return null
        }

    val topLevelDestination: List<Destination> = listOf(
        HOME, SUBSCRIPTION, SETTING
    )

    val subLevelDestination: List<Destination> = listOf(
        TRANSACTION_FORM, SUBSCRIPTION_FORM, CATEGORY
    )

    fun navigateToTopLevelDestination(topLevelDestination: Destination) {
        trace("Navigation: ${topLevelDestination.name}") {
            val topLevelNavOptions = navOptions {
                popUpTo(navController.graph.findStartDestination().id) {
                    saveState = true
                }
                launchSingleTop = true
                restoreState = true
            }

            when (topLevelDestination) {
                HOME -> navController.navigateToHome(navOptions = topLevelNavOptions)
                SUBSCRIPTION -> navController.navigateToSubscription(topLevelNavOptions)
                SETTING -> navController.navigateToSetting(topLevelNavOptions)
                else -> {}
            }
        }
    }
}

fun NavDestination?.isTopLevelDestinationInHierarchy(destination: Destination) =
    this?.hierarchy?.any {
        it.hasRoute(destination.route)
    } ?: false


