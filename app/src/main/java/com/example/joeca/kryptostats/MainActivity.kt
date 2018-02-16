package com.example.joeca.kryptostats

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ListView
import android.widget.Toast
import com.google.gson.GsonBuilder
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        recyclerView_main.layoutManager = LinearLayoutManager(this)

        fetchCurrencies()
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
