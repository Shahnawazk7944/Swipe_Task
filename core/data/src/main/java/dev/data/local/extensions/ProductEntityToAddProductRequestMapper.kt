package dev.data.local.extensions

import dev.data.local.entity.ProductEntity
import dev.models.AddProductRequest

fun List<ProductEntity>.toAddProductRequest(): List<AddProductRequest> {
    return this.map {
        AddProductRequest(
            productName = it.productName,
            productType = it.productType,
            price = it.price,
            tax = it.tax,
            images = it.image
        )
    }
}
