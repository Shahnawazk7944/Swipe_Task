package dev.swipe_task.presentation.features.products


import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.designsystem.components.CustomTopBar
import dev.designsystem.theme.Swipe_TaskTheme
import dev.designsystem.theme.spacing
import dev.models.Product
import dev.swipe_task.presentation.features.products.components.EmptyErrorScreen
import dev.swipe_task.presentation.features.products.components.ProductBottomSheet
import dev.swipe_task.presentation.features.products.components.ProductCard
import dev.swipe_task.presentation.features.products.components.ProductsShimmerEffect
import dev.swipe_task.presentation.features.products.components.SearchBarSection
import dev.swipe_task.presentation.features.products.viewmodels.ProductsEvents
import dev.swipe_task.presentation.features.products.viewmodels.ProductsStates
import dev.swipe_task.presentation.features.products.viewmodels.ProductsViewModel

@Composable
fun ProductsScreen(
    viewModel: ProductsViewModel = hiltViewModel(),
    onAddProductClick: () -> Unit,
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.productsEvents(ProductsEvents.GetProducts)
    }
    val activity = LocalActivity.current
    BackHandler {
        if (activity?.isTaskRoot == true) {
            activity.finishAndRemoveTask()
        }
    }

    val state = viewModel.state.collectAsStateWithLifecycle().value

    ProductsScreenContent(
        state = state,
        onBackClick = {
            if (activity?.isTaskRoot == true) {
                activity.finishAndRemoveTask()
            }
        },
        onAddProductClick = { onAddProductClick.invoke() },
        events = viewModel::productsEvents
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreenContent(
    state: ProductsStates,
    events: (ProductsEvents) -> Unit,
    onBackClick: () -> Unit,
    onAddProductClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
    val selectedProduct: MutableState<Product?> = remember {
        mutableStateOf(null)
    }
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = onBackClick,
                title = {
                    Text(
                        text = "Products",
                        style = MaterialTheme.typography.titleMedium,
                        textAlign = TextAlign.Center,
                    )
                },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = null
                )
            }
        },
    ) { paddingValues ->
        when {
            !state.loading -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = MaterialTheme.spacing.medium)
                ) {
                    SearchBarSection(
                        modifier = Modifier,
                        searchQuery = state.searchQuery,
                        onSearchQueryChange = { events(ProductsEvents.SearchProducts(it)) },
                    )
                    Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))

                    val filteredProducts = if (state.searchQuery.isEmpty()) {
                        state.products.sortedBy { it.productName }
                    } else {
                        state.products.filter {
                            it.productName?.contains(state.searchQuery, ignoreCase = true) == true
                        }.sortedBy { it.productName }
                    }

                    if (filteredProducts.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No products found",
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.secondary
                            )
                        }
                    } else {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding() + 60.dp),
                            horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
                        ) {
                            itemsIndexed(
                                filteredProducts,
                                key = { index, product -> "${product.hashCode() + index}" }
                            ) { index, product ->
                                ProductCard(
                                    product = product,
                                    onProductClick = {
                                        selectedProduct.value = product
                                    }
                                )
                            }
                        }
                    }
                }
            }

            state.failure != null -> {
                EmptyErrorScreen(state.failure.error.toString())
            }

            else -> {
                ProductsShimmerEffect(paddingValues)
            }
        }
        if (selectedProduct.value != null) {
            ProductBottomSheet(
                selectedProduct.value!!, onDismiss = {
                    selectedProduct.value = null
                },
                sheetState = sheetState
            )
        }
    }
}


@PreviewLightDark
@Composable
fun ProductsScreenContentPreview() {
    Swipe_TaskTheme {
        ProductsScreenContent(
            state = ProductsStates(
                products = listOf(
                    Product(
                        image = "",
                        productName = "Product 1",
                        productType = "Type 1",
                        price = 10.0,
                        tax = 20.0
                    ),
                    Product(
                        image = "",
                        productName = "Product 2",
                        productType = "Type 2",
                        price = 20.0,
                        tax = 30.0,
                    ),
                    Product(
                        image = "",
                        productName = "Product 3",
                        productType = "Type 3",
                        price = 30.0,
                        tax = 40.0
                    ),
                    Product(
                        image = "",
                        productName = "Product 4",
                        productType = "Type 4",
                        price = 40.0,
                        tax = 50.0
                    ),
                    Product(
                        image = "",
                        productName = "Product 5",
                        productType = "Type 5",
                        price = 50.0,
                        tax = 60.0
                    )
                )
            ),
            onBackClick = {},
            onAddProductClick = {},
            events = {}
        )
    }
}
