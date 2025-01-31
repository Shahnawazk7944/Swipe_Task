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
import java.io.IOException
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val api: Apis, private val context: Context
) : ProductRepository {

    override suspend fun addProductRequest(addProductRequest: AddProductRequest): Either<ErrorResponse, AddProductResponse> {
        return try {
            //tried many approaches but still image is not uploading i have to read more about but don't have time will look into it later
            fun uriToFile(context: Context, uri: Uri): File {
                val inputStream = context.contentResolver.openInputStream(uri)
                val file = File(context.cacheDir, "temp_file_${System.currentTimeMillis()}")
                try {
                    val outputStream = FileOutputStream(file)
                    inputStream?.copyTo(outputStream)
                    inputStream?.close()
                    outputStream.close()
                } catch (e: IOException) {
                    Log.e("FileError", "Failed to create file: ${e.message}")
                }
                return file
            }

            val files = addProductRequest.images.mapNotNull { uri ->
                val file = uriToFile(context, uri)
                if (!file.exists() || file.length() == 0L) {
                    Log.e("FileError", "File is empty or doesn't exist: ${file.path}")
                    null
                } else {
                    Log.d("FileUpload", "Uploading file: ${file.name} - ${file.path}")
                    val requestFile = file.asRequestBody("multipart/form-data".toMediaTypeOrNull())
                    MultipartBody.Part.createFormData("files", file.name, requestFile)
                }
            }

            if (files.isEmpty()) {
                Log.e("FileError", "No files were added to the request!")
            }

            val response = api.addProductRequest(
                productName = addProductRequest.productName.toRequestBody("text/plain".toMediaTypeOrNull()),
                productType = addProductRequest.productType.toRequestBody("text/plain".toMediaTypeOrNull()),
                price = addProductRequest.price.toRequestBody("text/plain".toMediaTypeOrNull()),
                tax = addProductRequest.tax.toRequestBody("text/plain".toMediaTypeOrNull()),
                files = files
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