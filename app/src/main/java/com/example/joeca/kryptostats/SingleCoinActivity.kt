package com.example.joeca.kryptostats

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.gson.GsonBuilder

import kotlinx.android.synthetic.main.activity_single_coin.*
import kotlinx.android.synthetic.main.fragment_single_coin.*
import okhttp3.*
import java.io.IOException

class SingleCoinActivity : AppCompatActivity() {

    lateinit var client: OkHttpClient
    lateinit var request: Request
    lateinit var name: String
//    putString("ticker", coinModel.Name)
//    putString("name", coinModel.CoinName)
//    putString("url", coinModel.Url)
//    putString("imageUrl", coinModel.ImageUrl)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_coin)

        this.name = intent.getStringExtra("ticker")

//        textView_ticker.text = intent.getStringExtra("ticker")
        fab.setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }

        getPriceData()
    }

    fun getPriceData() {
        client = OkHttpClient()
        request = Request
            .Builder()
            .url(String.format("https://www.cryptocompare.com/api/data/price?fsym=${this.name}&tsyms=USD"))
            .build()


        client
            .newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Log.d("ERROR", e.toString())
                }

                override fun onResponse(call: Call?, response: Response?) {
                    val body = response?.body()?.string()
                    val gson = GsonBuilder().create()
                    val responseJson = gson.fromJson<CoinPriceResponseModel>(body, CoinPriceResponseModel::class.java)
                    val coinPrice = responseJson.Data

                    println(coinPrice)
                }
            })
    }

}

data class CoinPriceModel(
    val Symbol: String,
    val Price: Float,
    val Open24Hour: Float,
    val Volume24Hours: Float
)

data class CoinPriceResponseModel(
    val Response: String,
    val Message: String,
    val Data: List<CoinPriceModel>
)