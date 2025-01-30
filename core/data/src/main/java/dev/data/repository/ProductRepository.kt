package dev.data.repository

import arrow.core.Either
import dev.models.AddProductRequest
import dev.models.AddProductResponse
import dev.models.ErrorResponse
import dev.models.Product

interface ProductRepository {
    suspend fun addProductRequest(
        addProductRequest: AddProductRequest
    ): Either<ErrorResponse, AddProductResponse>

    suspend fun getProducts(): Either<ErrorResponse, List<Product>>
}