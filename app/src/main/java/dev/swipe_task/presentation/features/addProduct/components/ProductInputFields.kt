package dev.swipe_task.presentation.features.addProduct.components

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.CurrencyRupee
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.DevicesOther
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.PhoneIphone
import androidx.compose.material.icons.filled.Phonelink
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import dev.designsystem.components.OutlinedInputField
import dev.designsystem.theme.spacing
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductEvents
import dev.swipe_task.presentation.features.addProduct.viewmodels.AddProductStates

@Composable
fun ProductInputFields(
    state: AddProductStates,
    events: (AddProductEvents) -> Unit
) {
    var productTypeExpanded by remember { mutableStateOf(false) }

    Column {
        OutlinedInputField(
            value = state.productName,
            onChange = {
                events(AddProductEvents.ProductNameChanged(it))
            },
            label = "Product Name",
            placeholder = { Text(text = "IPhone 16", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.Inventory2,
                    contentDescription = "Product icon",
                    modifier = Modifier.size(20.dp)
                )
            },
            error = state.productNameError,
            maxLines = 1
        )
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedInputField(
                value = state.productType,
                onChange = {},
                label = "Product Type",
                placeholder = {
                    Text(
                        text = "Smart Phone",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                readOnly = true,
                enabled = false,
                modifier = Modifier
                    .fillMaxWidth()
                    .pointerInput(Unit) { detectTapGestures { productTypeExpanded = true } },
                leadingIcon = {
                    Icon(
                        Icons.Default.Category,
                        contentDescription = "Category icon",
                        modifier = Modifier.size(20.dp)
                    )
                },
                error = state.productTypeError,
                maxLines = 1
            )

            DropdownMenu(
                expanded = productTypeExpanded,
                onDismissRequest = { productTypeExpanded = false },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = MaterialTheme.spacing.medium)
            ) {
                listOf("Smart Phone", "Electronics", "Wearables", "Others").forEach { type ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = type,
                                style = MaterialTheme.typography.titleMedium,
                                color = colorScheme.tertiary
                            )
                        },
                        onClick = {
                            productTypeExpanded = false
                            events(AddProductEvents.ProductTypeChanged(type))
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = when (type) {
                                    "Smart Phone" -> Icons.Default.PhoneIphone
                                    "Electronics" -> Icons.Default.Phonelink
                                    "Wearables" -> Icons.Default.Watch
                                    else -> Icons.Default.DevicesOther
                                },
                                tint = colorScheme.primary,
                                contentDescription = type
                            )
                        }
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(MaterialTheme.spacing.medium))

        OutlinedInputField(
            value = state.price,
            onChange = { events(AddProductEvents.PriceChanged(it)) },
            label = "Product Price",
            placeholder = { Text(text = "96000", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.CurrencyRupee,
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
            onChange = { events(AddProductEvents.TaxChanged(it)) },
            label = "Product Tax",
            placeholder = { Text(text = "28.00", style = MaterialTheme.typography.bodyMedium) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = {
                Icon(
                    Icons.Default.Description,
                    contentDescription = "Tax icon",
                    modifier = Modifier.size(20.dp)
                )
            },
            error = state.taxError,
            maxLines = 1
        )
    }
}