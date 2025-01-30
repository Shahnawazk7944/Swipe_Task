package dev.swipe_task.presentation.features.addProduct


import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Inventory2
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.designsystem.components.CustomTopBar
import dev.designsystem.components.LoadingDialog
import dev.designsystem.components.OutlinedInputField
import dev.designsystem.components.PrimaryButton
import dev.designsystem.theme.Swipe_TaskTheme
import dev.designsystem.theme.spacing
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductEvents
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductStates
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductViewModel
import dev.swipe_task.presentation.features.utils.isAddProductFormValid

@Composable
fun AddProductScreen(
    viewModel: AddProductViewModel = hiltViewModel(),
    onBackClick: () -> Unit
) {

    val state by viewModel.state.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    LaunchedEffect(key1 = state.failure) {
        state.failure?.let {
            snackbarHostState.showSnackbar(
                message = state.failure?.error ?: "Unknown error",
                duration = SnackbarDuration.Short,
                withDismissAction = true
            )
            viewModel.addProductEvents(AddProductEvents.ClearFailure)
        }
    }

    AddProductScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = {
            onBackClick.invoke()
        },
        onAddProductClick = { },
        events = viewModel::addProductEvents
    )
}


@Composable
fun AddProductScreenContent(
    state: AddProductStates,
    snackbarHostState: SnackbarHostState,
    events: (AddProductEvents) -> Unit,
    onBackClick: () -> Unit,
    onAddProductClick: () -> Unit
) {
    Scaffold(
        topBar = {
            CustomTopBar(
                onBackClick = onBackClick,
                title = {
                    Text(
                        text = "Add Product",
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
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.productName,
                onChange = {
                    events(AddProductEvents.ProductNameChanged(it))
                },
                label = "Product Name",
                placeholder = {
                    Text(
                        text = "IPhone 16",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Inventory2,
                        contentDescription = "Product icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.productNameError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.productType,
                onChange = {
                    events(AddProductEvents.ProductTypeChanged(it))
                },
                label = "Product Type",
                placeholder = {
                    Text(
                        text = "Smart Phone",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Category,
                        contentDescription = "Category icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.productTypeError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.price,
                onChange = {
                    events(AddProductEvents.PriceChanged(it))
                },
                label = "Product Price",
                placeholder = {
                    Text(
                        text = "96000",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.CurrencyRupee,
                        contentDescription = "Price icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.priceError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            OutlinedInputField(
                value = state.tax,
                onChange = {
                    events(AddProductEvents.TaxChanged(it))
                },
                label = "Product Tax",
                placeholder = {
                    Text(
                        text = "28.00",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Description,
                        contentDescription = "Tax icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.taxError,
                maxLines = 1
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.mediumLarge))
            PrimaryButton(
                onClick = { onAddProductClick.invoke() },
                shape = MaterialTheme.shapes.large,
                label = "Add Product",
                modifier = Modifier.fillMaxWidth(),
                enabled = state.isAddProductFormValid()
            )
        }
    }
}

@PreviewLightDark
@Composable
fun AddProductScreenContentPreview() {
    Swipe_TaskTheme {
        AddProductScreenContent(
            snackbarHostState = remember { SnackbarHostState() },
            onBackClick = {},
            state = AddProductStates(),
            onAddProductClick = {},
            events = {}
        )
    }
}
