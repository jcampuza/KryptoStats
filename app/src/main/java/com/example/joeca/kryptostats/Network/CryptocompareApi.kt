package com.example.joeca.kryptostats.Network

import okhttp3.*
import javax.inject.Inject

/**
 * Created by campu on 4/9/2018.
 */

class CryptocompareApi @Inject constructor(
        private val client: OkHttpClient,
        private val baseUrl: String,
        private val minBaseUrl: String
) {
    private val requestBuilder = HttpUrl.parse(baseUrl)?.newBuilder()
    private val minRequestBuilder= HttpUrl.parse(minBaseUrl)?.newBuilder()

    fun createGetRequest(url: HttpUrl?): Call {
        println(url)
        return Request.Builder()
                .url(url)
                .get()
                .build()
                .let() {
                    client.newCall(it)
                }
    }

    fun coinPrice(query: String): Call {
        val urlBuilder = minRequestBuilder
                ?.addPathSegment("data")
                ?.addPathSegment("price")
                ?.addQueryParameter("fsym", query)
                ?.addQueryParameter("tsyms", "USD")

        return createGetRequest(urlBuilder?.build())
    }

    fun coinList(): Call {
        val urlBuilder = requestBuilder
                ?.addPathSegment("data")
                ?.addPathSegment("coinlist")

        return createGetRequest(urlBuilder?.build())
    }

//    fun fetchCoinPrice(coinName: String): Call {
//        val url = baseUrl + "price?fsyms=${coinName}&tsyms=USD"
//        val request = Request.Builder().url(url).build()
//        return client.newCall(request)
//    }
}