package dev.swipe_task.presentation.features.addProduct.viewmodels

import dev.models.ErrorResponse

data class AddProductStates(
    val loading: Boolean = false,
    val failure: ErrorResponse? = null,

    val productName: String = "",
    val productNameError: String? = null,

    val productType: String = "",
    val productTypeError: String? = null,

    val price: String = "",
    val priceError: String? = null,

    val tax: String = "",
    val taxError: String? = null,
)

sealed class AddProductEvents {
    data object AddProduct : AddProductEvents()
    data object ClearFailure : AddProductEvents()
    data class ProductNameChanged(val productName: String) : AddProductEvents()
    data class ProductTypeChanged(val productType: String) : AddProductEvents()
    data class PriceChanged(val price: String) : AddProductEvents()
    data class TaxChanged(val tax: String) : AddProductEvents()
}
