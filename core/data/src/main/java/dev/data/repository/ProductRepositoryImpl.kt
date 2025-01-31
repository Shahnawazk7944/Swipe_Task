package dev.data.repository

import android.content.Context
import android.net.Uri
import android.util.Log
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
import java.io.FileOutputStream
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: Apis, private val context: Context
) : ProductRepository {

    override suspend fun addProductRequest(addProductRequest: AddProductRequest): Either<ErrorResponse, AddProductResponse> {
        return try {
            fun uriToFile(context: Context, uri: Uri): File {
                val fileExtension =
                    context.contentResolver.getType(uri)?.split("/")?.lastOrNull() ?: "jpg"
                val file =
                    File(context.cacheDir, "temp_file_${System.currentTimeMillis()}.$fileExtension")

                val inputStream = context.contentResolver.openInputStream(uri)
                val outputStream = FileOutputStream(file)
                inputStream?.use { input -> outputStream.use { output -> input.copyTo(output) } }

                return file
            }

            val files = addProductRequest.images.mapNotNull { uri ->
                val file = uriToFile(context, uri)
                if (!file.exists() || file.length() == 0L) {
                    Log.e("FileError", "File is empty: ${file.path}")
                    null
                } else {
                    val mimeType = context.contentResolver.getType(uri) ?: "image/*"
                    val requestFile = file.asRequestBody(mimeType.toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("files[]", file.name, requestFile)
                }
            }

            val response = api.addProductRequest(
                productName = addProductRequest.productName.toRequestBody("text/plain".toMediaTypeOrNull()),
                productType = addProductRequest.productType.toRequestBody("text/plain".toMediaTypeOrNull()),
                price = addProductRequest.price.toRequestBody("text/plain".toMediaTypeOrNull()),
                tax = addProductRequest.tax.toRequestBody("text/plain".toMediaTypeOrNull()),
                files = files.toTypedArray()
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
            Log.e("API_ERROR", "Exception: ${e.message}")
            Either.Left(ErrorResponse(error = e.message.toString()))
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