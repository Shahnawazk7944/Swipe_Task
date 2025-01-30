package dev.data.remote

import dev.models.AddProductResponse
import dev.models.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface Apis {
    @Multipart
    @POST("add")
    suspend fun addProductRequest(
        @Part("product_name") productName: RequestBody,
        @Part("product_type") productType: RequestBody,
        @Part("price") price: RequestBody,
        @Part("tax") tax: RequestBody,
        @Part files: List<MultipartBody.Part> = emptyList(),
    ): Response<AddProductResponse>

    @GET("get")
    suspend fun getProducts(): Response<List<Product>>
}
