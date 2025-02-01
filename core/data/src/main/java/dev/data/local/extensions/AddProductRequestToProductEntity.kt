package dev.data.local.extensions

import dev.data.local.entity.ProductEntity
import dev.models.AddProductRequest

fun AddProductRequest.toProductEntity(): ProductEntity {
    return ProductEntity(
        productName = productName,
        productType = productType,
        price = price,
        tax = tax,
        image = images
    )
}