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
    private fun createGetRequest(url: HttpUrl?): Call {
        return Request.Builder()
                .url(url)
                .get()
                .build()
                .let() {
                    client.newCall(it)
                }
    }

    fun coinPrice(query: String): Call {
        val urlBuilder = HttpUrl.parse(minBaseUrl)
                ?.newBuilder()
                ?.addPathSegment("data")
                ?.addPathSegment("price")
                ?.addQueryParameter("fsym", query)
                ?.addQueryParameter("tsyms", "USD")

        return createGetRequest(urlBuilder?.build())
    }

    fun coinList(): Call {
        val urlBuilder = HttpUrl.parse(baseUrl)
                ?.newBuilder()
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