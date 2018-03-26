package com.example.joeca.kryptostats

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.widget.Toast
import com.example.joeca.kryptostats.Adapters.CoinItemAdapter
import com.example.joeca.kryptostats.Models.CoinModel
import com.example.joeca.kryptostats.Models.CryptoCompareCoinListResponse
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

  lateinit var adapter: CoinItemAdapter
  lateinit var client: OkHttpClient
  lateinit var request: Request
  var items = emptyMap<String, CoinModel>()


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    recyclerView_main.layoutManager = LinearLayoutManager(this)
    setupCoinItemAdapter()
//        fetchCurrencies()
    fetchCoinList()
  }

  private fun setupCoinItemAdapter() {
    adapter = CoinItemAdapter(recyclerView_main, this@MainActivity, items)

    recyclerView_main.adapter = adapter
  }

  fun fetchCoinList() {
    println("Fetching currencies")
    client = OkHttpClient()
    request = Request
        .Builder()
        .url(String.format("https://www.cryptocompare.com/api/data/coinlist"))
        .build()

    client.newCall(request)
        .enqueue(object : Callback {
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

  fun fetchCurrencies() {
    println("Attempting to fetch currencies")
    val url = "https://www.cryptocompare.com/api/data/coinlist"

    val request = Request.Builder().url(url).build()

    val client = OkHttpClient()

    client.newCall(request).enqueue(object : Callback {
      override fun onResponse(call: Call?, response: Response?) {
        val body = response?.body()?.string()
        println(body)


        val gson = GsonBuilder().create()
        val coinListResponse = gson.fromJson(body, CurrencyResponse::class.java)
        val tickers = coinListResponse.Data

        runOnUiThread {
          recyclerView_main.adapter = MainAdapter(tickers, applicationContext)
        }
      }

      override fun onFailure(call: Call?, e: IOException?) {
        println("Failed to execute API Request")
        Toast.makeText(applicationContext, "Ooops, failed to get Currencies", Toast.LENGTH_LONG).show()
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
