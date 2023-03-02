package com.astro.rocketapp.data.hilt

import com.astro.rocketapp.data.BuildConfig
import com.astro.rocketapp.data.cache.InMemoryCache
import com.astro.rocketapp.data.cache.RocketInMemoryCache
import com.astro.rocketapp.data.model.domain.RocketDetailModel
import com.astro.rocketapp.data.repository.RocketRepository
import com.astro.rocketapp.data.repository.RocketRepositoryImpl
import com.astro.rocketapp.data.service.RocketService
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonNamingStrategy
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object DataModule {

    @Provides
    @Singleton
    fun provideLogInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor.Level.BODY
            } else {
                HttpLoggingInterceptor.Level.NONE
            }
        }
    }

    @Provides
    @Singleton
    fun provideHttpClient(loggingInterceptor: HttpLoggingInterceptor): OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor)
            .build()
    }



    @OptIn(ExperimentalSerializationApi::class)
    @Singleton
    @Provides
    fun provideRetrofit(httpClient: OkHttpClient): Retrofit {
        val baseUrl = "https://api.spacexdata.com/latest/"
        val contentType = "application/json".toMediaType()
        val json = Json {
            ignoreUnknownKeys = true
            namingStrategy = JsonNamingStrategy.SnakeCase
        }
        return Retrofit.Builder()
            .client(httpClient)
            .baseUrl(baseUrl)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }

    @Singleton
    @Provides
    fun provideRocketService(retrofit: Retrofit): RocketService {
        return retrofit.create(RocketService::class.java)
    }

    @Singleton
    @Provides
    fun provideRocketInMemoryCache(): InMemoryCache<String, RocketDetailModel> {
        return RocketInMemoryCache()
    }

    @Singleton
    @Provides
    fun provideRocketRepository(
        cache: InMemoryCache<String, RocketDetailModel>,
        service: RocketService
    ): RocketRepository {
        return RocketRepositoryImpl(cache, service)
    }
}
