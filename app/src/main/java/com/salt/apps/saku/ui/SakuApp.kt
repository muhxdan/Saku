package com.salt.apps.saku.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration.Short
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult.ActionPerformed
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.salt.apps.core.design.component.CustomTopAppBar
import com.salt.apps.core.design.icon.Icons
import com.salt.apps.feature.navigation.navigateToSubscriptionForm
import com.salt.apps.feature.navigation.navigateToTransaction
import com.salt.apps.feature.screens.category.components.dialog.composable.CategoryDialog
import com.salt.apps.saku.ui.navigation.Destination.CATEGORY
import com.salt.apps.saku.ui.navigation.Destination.HOME
import com.salt.apps.saku.ui.navigation.Destination.SETTING
import com.salt.apps.saku.ui.navigation.Destination.SUBSCRIPTION
import com.salt.apps.saku.ui.navigation.NavHost

@Composable
fun SakuApp(
    appState: AppState,
) {
    var showCategoryDialog by rememberSaveable { mutableStateOf(false) }

    if (showCategoryDialog) {
        CategoryDialog(onDismiss = { showCategoryDialog = false })
    }

    val snackbarHostState = remember { SnackbarHostState() }

    val currentDestination = appState.currentDestination

    val myNavigationSuiteItemColors = NavigationBarItemDefaults.colors(
        indicatorColor = MaterialTheme.colorScheme.background,
        selectedIconColor = MaterialTheme.colorScheme.primary,
        selectedTextColor = MaterialTheme.colorScheme.primary,
    )

    val destination = appState.destination
    val isDestinationTopLevel = destination in appState.topLevelDestination
    val isDestinationSubLevel = destination in appState.subLevelDestination

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        bottomBar = {
            AnimatedVisibility(
                visible = isDestinationTopLevel,
                enter = slideInVertically(
                    initialOffsetY = { it }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
                    )
                ),
                exit = slideOutVertically(
                    targetOffsetY = { it }, animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy, stiffness = Spring.StiffnessLow
                    )
                ),
            ) {
                NavigationBar(
                    containerColor = MaterialTheme.colorScheme.background,
                    contentColor = MaterialTheme.colorScheme.onBackground,
                ) {
                    appState.topLevelDestination.forEach { destination ->
                        val selected =
                            currentDestination.isTopLevelDestinationInHierarchy(destination)
                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (selected) {
                                        ImageVector.vectorResource(destination.selectedIcon)
                                    } else {
                                        ImageVector.vectorResource(destination.unselectedIcon)
                                    },
                                    contentDescription = null,
                                )
                            },
                            label = { Text(destination.iconTextId) },
                            onClick = { appState.navigateToTopLevelDestination(destination) },
                            colors = myNavigationSuiteItemColors,
                            selected = selected,
                        )
                    }
                }
            }
        },
        topBar = {
            CustomTopAppBar(
                title = destination?.titleTextId ?: "",
                actions = {
                    if (destination == CATEGORY || destination in appState.topLevelDestination && destination != SETTING) {
                        IconButton(
                            onClick = {
                                when (destination) {
                                    HOME -> appState.navController.navigateToTransaction()
                                    SUBSCRIPTION -> appState.navController.navigateToSubscriptionForm()
                                    CATEGORY -> showCategoryDialog = true
                                    else -> {}
                                }
                            },
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(Icons.Create),
                                contentDescription = null,
                            )
                        }
                    }
                },
                navigationIcon = {
                    if (isDestinationSubLevel) {
                        IconButton(
                            onClick = {
                                appState.navController.popBackStack()
                            },
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(Icons.ArrowBack),
                                contentDescription = null,
                            )
                        }
                    }
                },
            )

        },
    ) { padding ->
        NavHost(
            appState = appState,
            onShowSnackbar = { message, action ->
                snackbarHostState.showSnackbar(
                    message = message,
                    actionLabel = action,
                    duration = Short,
                ) == ActionPerformed
            },
            modifier = Modifier.padding(padding),
        )
    }
}

