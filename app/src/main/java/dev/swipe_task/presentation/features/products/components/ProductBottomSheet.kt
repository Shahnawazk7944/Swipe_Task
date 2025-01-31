package dev.swipe_task.presentation.features.products.components

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
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import dev.designsystem.components.PrimaryButton
import dev.designsystem.theme.spacing
import dev.models.Product


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductBottomSheet(
    product: Product,
    onDismiss: () -> Unit,
    sheetState: SheetState
) {
    ModalBottomSheet(
        containerColor = colorScheme.background,
        onDismissRequest = {
            onDismiss.invoke()
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(MaterialTheme.spacing.medium),
            horizontalAlignment = CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.small))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLarge * 2),
                contentAlignment = Alignment.Center
            ) {
                ProductCard(
                    product = product,
                    onProductClick = {}
                )
            }
            Spacer(modifier = Modifier.height(MaterialTheme.spacing.large))
            PrimaryButton(
                onClick = { onDismiss.invoke() },
                shape = RoundedCornerShape(MaterialTheme.spacing.large),
                label = "Okay",
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.extraLarge * 2)
            )
        }
    }
}