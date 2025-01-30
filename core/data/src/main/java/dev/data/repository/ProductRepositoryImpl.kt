package dev.data.repository

import arrow.core.Either
import dev.data.remote.Apis
import dev.models.AddProductRequest
import dev.models.AddProductResponse
import dev.models.ErrorResponse
import dev.models.Product
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: Apis
) : ProductRepository {

    override suspend fun addProductRequest(addProductRequest: AddProductRequest): Either<ErrorResponse, AddProductResponse> {
        return try {
            val files = addProductRequest.images.map { uri ->
                val file = File(uri.path)
                val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                MultipartBody.Part.createFormData(
                    "files", file.name, requestFile
                )
            }
            val response = api.addProductRequest(
                productName = addProductRequest.productName.toRequestBody("text/plain".toMediaTypeOrNull()),
                productType = addProductRequest.productType.toRequestBody("text/plain".toMediaTypeOrNull()),
                price = addProductRequest.price.toRequestBody("text/plain".toMediaTypeOrNull()),
                tax = addProductRequest.tax.toRequestBody("text/plain".toMediaTypeOrNull()),
                files = if (files.isNotEmpty()) {
                    files
                } else emptyList()
            )
            if (response.isSuccessful && response.body() != null) {
                Either.Right(response.body()!!)
            } else {
                Either.Left(
                    ErrorResponse(
                        error = response.errorBody()?.string() ?: "Unknown error"
                    )
                )
            }
        } catch (e: Exception) {
            Either.Left(
                ErrorResponse(
                    error = e.message.toString()
                )
            )
        }
    }

    override suspend fun getProducts(): Either<ErrorResponse, List<Product>> {
        return try {
            val response = api.getProducts()
            if (response.isSuccessful && response.body() != null) {
                Either.Right(response.body()!!)
            } else {
                Either.Left(
                    ErrorResponse(
                        error = response.errorBody()?.string() ?: "Unknown error"
                    )
                )
            }
        } catch (e: Exception) {
            Either.Left(ErrorResponse(error = e.message.toString()))
        }
    }
}