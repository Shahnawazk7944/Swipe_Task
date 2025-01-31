package dev.swipe_task.presentation.features.addProduct.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import dev.designsystem.components.PrimaryButton
import dev.designsystem.theme.spacing
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductEvents
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductStates
import dev.swipe_task.presentation.features.products.components.ProductCard


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddProductBottomSheet(
    state: AddProductStates,
    events: (AddProductEvents) -> Unit,
    sheetState: androidx.compose.material3.SheetState
) {
    ModalBottomSheet(
        containerColor = colorScheme.background,
        onDismissRequest = {
            events(AddProductEvents.ClearAddedProduct)
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = CenterHorizontally
        ) {
            Text(
                text = state.addedProduct?.message ?: "Product Added Successfully",
                style = MaterialTheme.typography.headlineSmall,
                color = colorScheme.primary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Text(
                text = "Product Id : ${state.addedProduct?.productId ?: "No id found"}",
                style = MaterialTheme.typography.bodyLarge,
                color = colorScheme.tertiary
            )
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLarge * 2),
                contentAlignment = Alignment.Center
            ) {
                ProductCard(
                    product = state.addedProduct?.productDetails ?: return@ModalBottomSheet,
                    onProductClick = {}
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            PrimaryButton(
                onClick = { events(AddProductEvents.ClearAddedProduct) },
                shape = RoundedCornerShape(MaterialTheme.spacing.large),
                label = "Okay",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLarge * 2)
            )
        }
    }
}