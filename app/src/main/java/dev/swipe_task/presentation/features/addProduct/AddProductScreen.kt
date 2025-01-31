package dev.swipe_task.presentation.features.addProduct


import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dev.designsystem.components.CustomTopBar
import dev.designsystem.components.LoadingDialog
import dev.designsystem.components.PrimaryButton
import dev.designsystem.components.uriToBitmap
import dev.designsystem.theme.Swipe_TaskTheme
import dev.designsystem.theme.spacing
import dev.models.AddProductRequest
import dev.models.AddProductResponse
import dev.models.Product
import dev.swipe_task.presentation.features.addProduct.components.AddProductBottomSheet
import dev.swipe_task.presentation.features.addProduct.components.ProductInputFields
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductEvents
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductStates
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductViewModel
import dev.swipe_task.presentation.features.utils.isAddProductFormValid
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
    val context = LocalContext.current

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                viewModel.addProductEvents(AddProductEvents.ProductImagesChanged(uri))
                CoroutineScope(Dispatchers.IO).launch {
                    val bitmap = uriToBitmap(context = context, uri = uri)
                    bitmap?.let {
                        viewModel.addProductEvents(AddProductEvents.ProductImageBitmapChanged(it))
                    }
                }
            }
        }
    AddProductScreenContent(
        state = state,
        snackbarHostState = snackbarHostState,
        onBackClick = {
            onBackClick.invoke()
        },
        onAddProductClick = {
            viewModel.addProductEvents(
                AddProductEvents.AddProduct(
                    addProductsRequest = AddProductRequest(
                        productName = state.productName,
                        productType = state.productType,
                        price = state.price,
                        tax = state.tax,
                        images = state.productImages
                    )
                )
            )
        },
        events = viewModel::addProductEvents,
        onAddImageClick = {
            launcher.launch("image/*")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductScreenContent(
    state: AddProductStates,
    snackbarHostState: SnackbarHostState,
    events: (AddProductEvents) -> Unit,
    onBackClick: () -> Unit,
    onAddImageClick: () -> Unit,
    onAddProductClick: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState(
        skipPartiallyExpanded = true,
    )
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
                    containerColor = colorScheme.error,
                    contentColor = colorScheme.onError,
                    snackbarData = it,
                    actionColor = colorScheme.secondary,
                    dismissActionContentColor = colorScheme.secondary
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
            LazyRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                item(key = "add_image") {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "",
                        tint = colorScheme.primary,
                        modifier = Modifier
                            .size(100.dp)
                            .padding(end = MaterialTheme.spacing.small)
                            .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                            .border(
                                2.dp,
                                colorScheme.primary,
                                RoundedCornerShape(MaterialTheme.spacing.small)
                            )
                            .clickable {
                                onAddImageClick.invoke()
                            }
                            .padding(MaterialTheme.spacing.medium)
                    )
                }

                items(state.imageBitmaps) { bitmap ->
                    if (bitmap != null) {
                        Image(
                            bitmap = bitmap.asImageBitmap(),
                            contentDescription = "",
                            modifier = Modifier
                                .size(100.dp)
                                .padding(end = MaterialTheme.spacing.small)
                                .clip(RoundedCornerShape(MaterialTheme.spacing.small))
                                .border(
                                    2.dp,
                                    colorScheme.primary,
                                    RoundedCornerShape(MaterialTheme.spacing.small)
                                ),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        CircularProgressIndicator()
                    }
                }
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))
            ProductInputFields(
                state = state,
                events = events
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

        if (state.addedProduct != null) {
            AddProductBottomSheet(state = state, events = events, sheetState = sheetState)
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
            state = AddProductStates(
                addedProduct = AddProductResponse(
                    message = "Product Added Successfully",
                    productId = 34,
                    productDetails = Product(
                        image = "",
                        productName = "Product 1",
                        productType = "Type 1",
                        price = 10.0,
                        tax = 20.0
                    )
                )
            ),
            onAddProductClick = {},
            events = {},
            onAddImageClick = {}
        )
    }
}
