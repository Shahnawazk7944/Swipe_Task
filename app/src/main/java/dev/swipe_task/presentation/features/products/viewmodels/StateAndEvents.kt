package dev.swipe_task.presentation.features.products.viewmodels

import dev.models.ErrorResponse
import dev.models.Product

data class ProductsStates(
    val loading: Boolean = false,
    val failure: ErrorResponse? = null,
    val products: List<Product> = emptyList()
)

sealed class ProductsEvents {
    data object GetProducts : ProductsEvents()
    data object ClearFailure : ProductsEvents()
}

