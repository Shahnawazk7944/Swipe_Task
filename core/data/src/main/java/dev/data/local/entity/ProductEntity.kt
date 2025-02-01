package dev.data.local.entity

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import dev.data.local.extensions.UriListTypeConvertor

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey
    val productName: String,
    val productType: String,
    val price: String,
    val tax: String,
    val image: List<Uri> = emptyList(),
)
