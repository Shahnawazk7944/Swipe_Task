package dev.swipe_task.presentation.features.addProduct.viewmodels

import android.graphics.Bitmap
import android.net.Uri
import dev.models.AddProductRequest
import dev.models.AddProductResponse
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

    val productImages: List<Uri> = emptyList(),
    val imageBitmaps: List<Bitmap?> = emptyList(),

    val addedProduct: AddProductResponse? = null,
    val isProductRequestAddedToLocalDb: String? = null,

    val isOnline: Boolean = false
)

sealed class AddProductEvents {
    data class AddProduct(val addProductsRequest: AddProductRequest) : AddProductEvents()
    data object ClearFailure : AddProductEvents()
    data class ProductNameChanged(val productName: String) : AddProductEvents()
    data class ProductTypeChanged(val productType: String) : AddProductEvents()
    data class PriceChanged(val price: String) : AddProductEvents()
    data class TaxChanged(val tax: String) : AddProductEvents()
    data class ProductImagesChanged(val productImage: Uri) : AddProductEvents()
    data class ProductImageBitmapChanged(val productImageBitmaps: Bitmap) : AddProductEvents()
    data object ClearAddedProduct : AddProductEvents()
}
