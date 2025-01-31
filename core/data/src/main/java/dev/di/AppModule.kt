package dev.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.components.SingletonComponent
import dev.data.remote.Apis
import dev.data.repository.ProductRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import dev.data.repository.ProductRepositoryImpl
import dev.data.util.Constants.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

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
}

@InstallIn(ActivityRetainedComponent::class)
@Module
object RepositoryModule {
    @Provides
    fun provideProductRepository(
        apis: Apis,
        @ApplicationContext context: Context
    ): ProductRepository {
        return ProductRepositoryImpl(apis,context)
    }
}
