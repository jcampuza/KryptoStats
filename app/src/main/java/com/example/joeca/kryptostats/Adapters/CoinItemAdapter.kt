package com.example.joeca.kryptostats.Adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.text.Layout
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.joeca.kryptostats.Models.CoinModel
import com.example.joeca.kryptostats.R
import com.example.joeca.kryptostats.SingleCoinActivity
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.coin_layout.view.*

/**
 * Created by Joseph Campuzano on 3/25/2018.
 */

typealias CoinMap = Map<String, CoinModel>

class CoinItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
  val coinIcon = itemView.coinIcon
  val coinFullName = itemView.coinFullName
  val coinSymbol = itemView.coinSymbol
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

        Toast.makeText(activity, "Navigating to ${coinModel.Name} View", Toast.LENGTH_SHORT).show()
      }
    }
  }

  override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): CoinItemViewHolder {
    val view = LayoutInflater.from(parent?.context)
        .inflate(R.layout.coin_layout, parent, false)

    return CoinItemViewHolder(view)
  }

  override fun getItemCount(): Int {
    return itemsList.size
  }
}