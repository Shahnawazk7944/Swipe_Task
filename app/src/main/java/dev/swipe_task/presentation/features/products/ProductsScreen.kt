package dev.swipe_task.presentation.features.products


import androidx.activity.compose.BackHandler
import androidx.activity.compose.LocalActivity
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.designsystem.components.CustomTopBar
import dev.designsystem.components.LoadingDialog
import dev.designsystem.components.NetworkImage
import dev.designsystem.theme.Swipe_TaskTheme
import dev.designsystem.theme.spacing
import dev.models.Product
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

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.failure) {
        state.failure?.let {
            snackbarHostState.showSnackbar(
                message = state.failure?.error ?: "Unknown error",
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.productsEvents(ProductsEvents.ClearFailure)
        }
    }

    ProductsScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = {
            if (activity?.isTaskRoot == true) {
                activity.finishAndRemoveTask()
            }
        },
        onAddProductClick = { onAddProductClick.invoke() }
    )
}


@Composable
fun ProductsScreenContent(
    state: ProductsStates,
    snackbarHostState: SnackbarHostState,
    onBackClick: () -> Unit,
    onAddProductClick: () -> Unit
) {
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
        snackbarHost = {
            SnackbarHost(
                hostState = snackbarHostState,
            ) {
                Snackbar(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                    snackbarData = it,
                    actionColor = MaterialTheme.colorScheme.secondary,
                    dismissActionContentColor = MaterialTheme.colorScheme.secondary
                )
            }
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAddProductClick) {
                Icon(
                    imageVector = Icons.Filled.Add, contentDescription = null
                )
            }
        },
    ) { paddingValues ->
        if (state.loading) {
            LoadingDialog(true)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = MaterialTheme.spacing.medium)
        ) {

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier = Modifier
                    .fillMaxSize(),
                contentPadding = PaddingValues(bottom = paddingValues.calculateBottomPadding() + 60.dp),
                horizontalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall),
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                itemsIndexed(
                    state.products.sortedBy { it.productName },
                    key = { index, product -> "${product.hashCode() + index}" },
                ) { index, product ->
                    ProductCard(
                        product = product,
                        onProductClick = {})
                }
            }
        }
    }
}

@Composable
fun ProductCard(
    product: Product,
    onProductClick: (Product) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = 300.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onBackground
        ),
        shape = RoundedCornerShape(MaterialTheme.spacing.medium),
    ) {
        Column(
            modifier = Modifier
                .clickable { onProductClick(product) }
                .wrapContentHeight()
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.small)
        ) {
            NetworkImage(
                url = product.image,
                contentDescription = product.productName,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier
                    .height(200.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.secondary.copy(alpha = 0.2f),
                        RoundedCornerShape(8.dp)
                    ),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(MaterialTheme.spacing.extraSmall)
            ) {
                Text(
                    text = product.productName ?: "Unnamed Product",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    text = product.productType ?: "Unknown Type",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(MaterialTheme.spacing.extraSmall))

                Text(
                    text = "$${product.price ?: "0.0"} (Tax: ${product.tax ?: "N/A"})",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
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
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            onAddProductClick = {}
        )
    }
}
