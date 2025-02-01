package dev.data.local

import dev.data.local.dao.ProductDao
import dev.data.local.extensions.toAddProductRequest
import dev.data.local.extensions.toProductEntity
import dev.models.AddProductRequest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

class ProductLocalDataSource @Inject constructor(
    private val dao: ProductDao,
) {

    suspend fun insertProduct(addProductRequest: AddProductRequest) {
        val productEntity = addProductRequest.toProductEntity()
        dao.insertProduct(productEntity)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getAllProducts(): Flow<List<AddProductRequest>> {
        return dao.getAllProducts().mapLatest { it.toAddProductRequest() }
    }

    suspend fun deleteAllProducts() {
        dao.deleteAllProducts()
    }

}