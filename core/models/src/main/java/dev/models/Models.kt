package dev.models

import android.net.Uri
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import java.net.URI

@Serializable
data class Product(
    val image: String? = null,
    val price: Double? = null,
    @SerializedName("product_name")
    val productName: String? = null,
    @SerializedName("product_type")
    val productType: String? = null,
    val tax: Double? = null,
)

data class AddProductRequest(
    val productName: String,
    val productType: String,
    val price: String,
    val tax: String,
    val images: List<Uri> = emptyList()
)

@Serializable
data class AddProductResponse(
    val message: String? = null,
    @SerializedName("product_details") val productDetails: Product? = null,
    @SerializedName("product_id") val productId: Int? = null,
    val success: Boolean? = null
)

@Serializable
data class ErrorResponse(
    val error: String? = null,
)