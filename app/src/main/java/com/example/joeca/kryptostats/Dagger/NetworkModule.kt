package com.example.joeca.kryptostats.Dagger

import com.example.joeca.kryptostats.Network.CryptocompareApi
import dagger.Module
import dagger.Provides
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import javax.inject.Named
import javax.inject.Singleton

/**
 * Created by campu on 4/9/2018.
 */

@Module
class NetworkModule {

    companion object {
        private const val CRYPTO_BASE_URL = "https://www.cryptocompare.com/api"
        private const val CRYPTO_MIN_BASE_URL = "https://min-api.cryptocompare.com"
    }

    @Provides
    @Named(CRYPTO_BASE_URL)
    fun provideCryptoBaseUrl() = CRYPTO_BASE_URL

    @Provides
    @Named(CRYPTO_MIN_BASE_URL)
    fun provideCryptoMinBaseUrl() = CRYPTO_MIN_BASE_URL

    @Provides
    @Singleton
    fun provideHttpClient() = OkHttpClient()

    @Provides
    @Singleton
    fun provideRequestBuilder(@Named(CRYPTO_BASE_URL) baseUrl: String) =
            HttpUrl.parse(baseUrl)?.newBuilder()

    @Provides
    @Singleton
    fun provideMinApiRequestBuilder(@Named(CRYPTO_MIN_BASE_URL) baseUrl: String) =
            HttpUrl.parse(baseUrl)?.newBuilder()

    @Provides
    @Singleton
    fun provideCryptoCompareApi(
            client: OkHttpClient
    ) = CryptocompareApi(client, CRYPTO_BASE_URL, CRYPTO_MIN_BASE_URL)
}