package com.salt.apps.saku.ui.navigation

import com.salt.apps.core.design.icon.Icons
import com.salt.apps.feature.navigation.CategoryRoute
import com.salt.apps.feature.navigation.HomeRoute
import com.salt.apps.feature.navigation.SettingRoute
import com.salt.apps.feature.navigation.SubscriptionFormRoute
import com.salt.apps.feature.navigation.SubscriptionRoute
import com.salt.apps.feature.navigation.TransactionRoute
import kotlin.reflect.KClass

enum class Destination(
    val selectedIcon: Int,
    val unselectedIcon: Int,
    val iconTextId: String,
    val titleTextId: String,
    val route: KClass<*>,
) {
    HOME(
        selectedIcon = Icons.HomeFilled,
        unselectedIcon = Icons.Home,
        iconTextId = "Home",
        titleTextId = "Home",
        route = HomeRoute::class,
    ),

    SUBSCRIPTION(
        selectedIcon = Icons.SubscriptionsFilled,
        unselectedIcon = Icons.Subscriptions,
        iconTextId = "Subscription",
        titleTextId = "Subscription",
        route = SubscriptionRoute::class,
    ),
    SETTING(
        selectedIcon = Icons.SettingFilled,
        unselectedIcon = Icons.Setting,
        iconTextId = "Setting",
        titleTextId = "Setting",
        route = SettingRoute::class,
    ),
    CATEGORY(
        selectedIcon = Icons.CategoryFilled,
        unselectedIcon = Icons.Category,
        iconTextId = "Category",
        titleTextId = "Category",
        route = CategoryRoute::class,
    ),
    TRANSACTION_FORM(
        selectedIcon = Icons.SettingFilled,
        unselectedIcon = Icons.Setting,
        iconTextId = "Setting",
        titleTextId = "Add new transaction",
        route = TransactionRoute::class,
    ),
    SUBSCRIPTION_FORM(
        selectedIcon = Icons.SettingFilled,
        unselectedIcon = Icons.Setting,
        iconTextId = "Setting",
        titleTextId = "Add new subscription",
        route = SubscriptionFormRoute::class,
    ),
}
