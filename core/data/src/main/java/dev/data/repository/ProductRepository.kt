package dev.data.repository

import arrow.core.Either
import dev.models.AddProductRequest
import dev.models.AddProductResponse
import dev.models.ErrorResponse
import dev.models.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    suspend fun addProductRequest(
        addProductRequest: AddProductRequest
    ): Either<ErrorResponse, AddProductResponse>

    suspend fun getProducts(): Either<ErrorResponse, List<Product>>

    suspend fun insertProduct(addProductRequest: AddProductRequest) : Either<Unit, Unit>

    suspend fun getAllProducts(): Flow<List<AddProductRequest>>

    suspend fun deleteAllProducts() : Either<Unit, Unit>
}