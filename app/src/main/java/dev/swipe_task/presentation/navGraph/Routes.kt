package dev.swipe_task.presentation.navGraph

import kotlinx.serialization.Serializable

@Serializable
sealed class Routes {
    @Serializable
    data object ProductsScreen : Routes()
    @Serializable
    data object AddProductScreen : Routes()
}