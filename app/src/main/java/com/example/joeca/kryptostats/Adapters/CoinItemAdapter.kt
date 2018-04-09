package com.example.joeca.kryptostats.Adapters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.joeca.kryptostats.Activities.CurrencyResponse
import com.example.joeca.kryptostats.Models.CoinModel
import com.example.joeca.kryptostats.R
import com.example.joeca.kryptostats.Activities.SingleCoinActivity
import com.example.joeca.kryptostats.R.id.recyclerView_main
import com.google.gson.GsonBuilder
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.coin_layout.view.*
import okhttp3.*
import java.io.IOException

/**
 * Created by Joseph Campuzano on 3/25/2018.
 */

typealias CoinMap = Map<String, CoinModel>

class CoinItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val coinIcon = itemView.coinIcon
  val coinFullName = itemView.coinFullName
  val coinSymbol = itemView.coinSymbol
  val coinPrice = itemView.coinPriceUsd
}

class CoinItemAdapter(recyclerView: RecyclerView, internal var activity: Activity, var items: CoinMap) : RecyclerView.Adapter<CoinItemViewHolder>() {

  var itemsList = toList()
  var isLoaded: Boolean = false;
  var visibleThreshold = 5
  var lastVisibleItem: Int = 0
  var totalCount: Int = 0

  fun toList(): MutableList<Pair<String, CoinModel>> {
    return items.toList().toMutableList()
  }

  fun setIsLoaded(loaded: Boolean) {
    this.isLoaded = loaded
  }

  fun updateData(coinItems: Map<String, CoinModel>) {
    this.items = coinItems
    itemsList = toList()

    println("Updated data")
    notifyDataSetChanged()
  }

  override fun onBindViewHolder(holder: CoinItemViewHolder?, position: Int) {
    val coinModel = itemsList[position].second

    val item = holder as CoinItemViewHolder

    item.apply {
      coinSymbol.text = coinModel.Name
      coinFullName.text = coinModel.CoinName
      coinPrice.text = "100"

      Picasso
          .get()
          .load("https://www.cryptocompare.com${coinModel.ImageUrl}")
          .into(coinIcon)

      itemView.setOnClickListener {
        val context = holder.itemView.context
        val intent: Intent = Intent(context, SingleCoinActivity::class.java)
        val extras = Bundle();

        extras.apply {
          putString("ticker", coinModel.Name)
          putString("name", coinModel.CoinName)
          putString("url", coinModel.Url)
          putString("imageUrl", coinModel.ImageUrl)
        }

        intent.putExtras(extras)
        context.startActivity(intent)
      }
    }
  }

//  fun fetchCoinPrice(coinItem: CoinModel) {
//    val url = "https://www.cryptocompare.com/api/data/price?fsym=${coinItem.Name}"
//    val request = Request.Builder().url(url).build()
//
//    val client = OkHttpClient()
//
//    client.newCall(request).enqueue(object : Callback {
//      override fun onResponse(call: Call?, response: Response?) {
//        val body = response?.body()?.string()
//        println(body)
//
//
//        val gson = GsonBuilder().create()
//        val coinListResponse = gson.fromJson(body, CurrencyResponse::class.java)
//        val tickers = coinListResponse.Data
//
//        runOnUiThread {
//          recyclerView_main.adapter = MainAdapter(tickers, applicationContext)
//        }
//      }
//
//      override fun onFailure(call: Call?, e: IOException?) {
//        println("Failed to execute API Request")
//        Toast.makeText(applicationContext, "Ooops, failed to get Currencies", Toast.LENGTH_LONG).show()
//      }
//    })
//  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CoinItemViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.coin_layout, parent, false)

    return CoinItemViewHolder(view)
  }

  override fun getItemCount(): Int {
    return itemsList.size
  }
}