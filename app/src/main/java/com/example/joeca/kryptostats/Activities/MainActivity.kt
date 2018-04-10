package com.example.joeca.kryptostats.Activities

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.joeca.kryptostats.Adapters.CoinItemAdapter
import com.example.joeca.kryptostats.Application.KryptoApplication
import com.example.joeca.kryptostats.Models.CoinModel
import com.example.joeca.kryptostats.Models.CryptoCompareCoinListResponse
import com.example.joeca.kryptostats.R
import com.example.joeca.kryptostats.Network.CryptocompareApi
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject lateinit var cryptoCompareApi: CryptocompareApi

    lateinit var adapter: CoinItemAdapter
//    lateinit var cryptoApi: CryptocompareApi
    var items = emptyMap<String, CoinModel>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as KryptoApplication).kryptoComponent.inject(this)

        recyclerView_main.layoutManager = LinearLayoutManager(this)
//        cryptoApi = CryptocompareApi()
        setupCoinItemAdapter()
        fetchCoinList()
    }

    private fun setupCoinItemAdapter() {
        adapter = CoinItemAdapter(recyclerView_main, this@MainActivity, items, cryptoCompareApi)

        recyclerView_main.adapter = adapter
    }

    fun fetchCoinList() {
        cryptoCompareApi.coinList().enqueue(object : Callback {
            override fun onFailure(call: Call?, e: IOException?) {
                Log.d("ERROR", e.toString())
                Toast.makeText(applicationContext, "Oops, Failed to fetch Currencies", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call?, response: Response?) {
                val body = response?.body()?.string()
                val gson = GsonBuilder().create()
                val responseJson = gson.fromJson<CryptoCompareCoinListResponse>(body, CryptoCompareCoinListResponse::class.java)
                val coinItems = responseJson.Data

                runOnUiThread {
                    adapter.setIsLoaded(true)
                    adapter.updateData(coinItems)
                }
            }
        })
    }
}

data class CurrencyResponse(
        val Response: String,
        val Message: String,
        val Data: Map<String, Ticker>
)

data class Ticker(
        val Id: String,
        val Url: String,
        val ImageUrl: String,
        val Name: String,
        val CoinName: String
)
