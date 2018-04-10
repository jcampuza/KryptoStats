package com.example.joeca.kryptostats.Models

import okhttp3.Response
import org.json.JSONObject

/**
 * Created by Joseph Campuzano on 3/25/2018.
 */

data class CoinModel(
        val Id: String,
        val Url: String,
        val ImageUrl: String,
        val Name: String,
        val CoinName: String,
        val ProofType: String,
        var Price: Double,
        val Algorithm: String,
        val SortOrder: Int
)

data class CryptoCompareCoinListResponse(
        val Response: String,
        val Message: String,
        val BaseImageUrl: String,
        val BaseLinkUrl: String,
        val Data: Map<String, CoinModel>
)

data class CryptoCompareCoinPriceResponse(private val response: Response) {
    val prices: MutableMap<String, Double> = mutableMapOf()

    init {
        val body = response.body()?.string()
        println(body)
        val json = JSONObject(body)
        json.keys().forEach {
            prices[it] = json.getDouble(it)
        }
    }
}

/*
Full Response from CryptoCompare CoinList

{
	"Response": "Success",
	"Message": "Coin list succesfully returned!",
	"BaseImageUrl": "https://www.cryptocompare.com",
	"BaseLinkUrl": "https://www.cryptocompare.com",
	"Data": {
		"LTC": {
			"Id": "3808",
			"Url": "/coins/ltc/overview",
			"ImageUrl": "/media/19782/ltc.png",
			"Name": "LTC",
			"CoinName": "Litecoin",
			"FullName": "Litecoin (LTC)",
			"Algorithm": "Scrypt",
			"ProofType": "PoW",
			"SortOrder": "2"
		}
	    ...
	},
	"Type": 100
}

 */