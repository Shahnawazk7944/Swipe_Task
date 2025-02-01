package dev.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import dev.data.remote.Apis
import dev.data.repository.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.data.local.ProductDatabase
import dev.data.local.ProductLocalDataSource
import dev.data.local.dao.ProductDao
import dev.data.local.extensions.UriListTypeConvertor
import dev.data.repository.ConnectivityRepository
import dev.data.repository.ConnectivityRepositoryImpl
import dev.data.repository.ProductRepositoryImpl
import dev.data.util.Constants.BASE_URL
import dev.data.util.Constants.PRODUCT_DATABASE_NAME
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    @Singleton
    fun provideApis(): Apis {
        return Retrofit.Builder()
            .baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Apis::class.java)
    }

    @Singleton
    @Provides
    fun provideProductDatabase(@ApplicationContext context: Context): ProductDatabase {
        return Room.databaseBuilder(
            context,
            ProductDatabase::class.java,
            PRODUCT_DATABASE_NAME
        ).addTypeConverter(UriListTypeConvertor())
            .fallbackToDestructiveMigration()
            .build()
    }

    @Singleton
    @Provides
    fun provideProductDao(productDatabase: ProductDatabase): ProductDao {
        return productDatabase.productDao()
    }
}

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepositoryModule {
    @Provides
    fun provideProductLocalDataSource(
        dao: ProductDao,
    ): ProductLocalDataSource {
        return ProductLocalDataSource(dao)
    }

    @Provides
    fun provideProductRepository(
        apis: Apis,
        @ApplicationContext context: Context,
        productLocalDataSource: ProductLocalDataSource
    ): ProductRepository {
        return ProductRepositoryImpl(apis,context, productLocalDataSource)
    }

    @Provides
    fun provideConnectivityRepository(
        @ApplicationContext context: Context
    ): ConnectivityRepository {
        return ConnectivityRepositoryImpl(context)
    }
}
