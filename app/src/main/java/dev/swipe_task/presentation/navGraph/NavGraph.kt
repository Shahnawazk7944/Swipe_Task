package dev.swipe_task.presentation.navGraph

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dev.swipe_task.presentation.features.addProduct.AddProductScreen
import dev.swipe_task.presentation.features.products.ProductsScreen


@Composable
fun Swipe_TaskNavGraph(
    navController: NavHostController = rememberNavController(),
) {
    NavHost(
        modifier = Modifier
            .fillMaxSize()
            .imePadding(),
        navController = navController,
        startDestination = Routes.ProductsScreen
    ) {
        composable<Routes.ProductsScreen> {
            ProductsScreen(
                onAddProductClick = {
                    navController.navigate(Routes.AddProductScreen)
                }
            )
        }
        composable<Routes.AddProductScreen> {
            AddProductScreen(
                onBackClick = {
                    navController.navigateUp()
                }
            )
        }
    }
}
