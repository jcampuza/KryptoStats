package com.example.joeca.kryptostats

import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso

import kotlinx.android.synthetic.main.activity_single_coin.*
import okhttp3.*
import java.io.IOException

class SingleCoinActivity : AppCompatActivity() {

  lateinit var client: OkHttpClient
  lateinit var request: Request
  lateinit var name: String
  lateinit var imageUrl: String
//    putString("ticker", coinModel.Name)
//    putString("name", coinModel.CoinName)
//    putString("url", coinModel.Url)
//    putString("imageUrl", coinModel.ImageUrl)

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_single_coin)

    this.apply {
      name = intent.getStringExtra("ticker")
      imageUrl = intent.getStringExtra("imageUrl")
    }

    coinSymbol.text = name
    coinFullName.text = name

    Picasso.get()
        .load("https://www.cryptocompare.com${imageUrl}")
        .into(coinIcon)

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
        .url(String.format("https://min-api.cryptocompare.com/data/pricemultifull?fsyms=${this.name}&tsyms=USD"))
        .build()


    client
        .newCall(request)
        .enqueue(object : Callback {
          override fun onFailure(call: Call?, e: IOException?) {
            Log.d("ERROR", e.toString())
          }

          override fun onResponse(call: Call?, response: Response?) {
            val body = response?.body()?.string()
            val gson = GsonBuilder().create()
            val responseJson = gson.fromJson<UpdatedCoinPriceResponseModel>(body, UpdatedCoinPriceResponseModel::class.java)
            val coinPrice = responseJson.RAW[name]
            val coinModel = responseJson.RAW[name]!!["USD"]
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

data class RawCoinPriceModel(
    val PRICE: Float,
    val LASTUPDATE: Int,
    val VOLUMEDAY: Float,
    val VOLUME24HOUR: Float,
    val OPENDAY: Float,
    val HIGHDAY: Float,
    val LOWDAY: Float,
    val OPEN24HOUR: Float,
    val HIGH24HOUR: Float,
    val LOW24HOUR: Float,
    val CHANGE24HOUR: Float,
    val CHANGEPCT24HOUR: Float,
    val CHANGEDAY: Float,
    val CHANGEPCTDAY: Float,
    val MKTCAP: Float
)

data class UpdatedCoinPriceResponseModel(
    val RAW: Map<String, Map<String, RawCoinPriceModel>>
)