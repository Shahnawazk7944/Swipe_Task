package dev.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import dev.data.local.dao.ProductDao
import dev.data.local.entity.ProductEntity
import dev.data.local.extensions.UriListTypeConvertor

@Database(version = 1, entities = [ProductEntity::class])
@TypeConverters(UriListTypeConvertor::class)
abstract class ProductDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}