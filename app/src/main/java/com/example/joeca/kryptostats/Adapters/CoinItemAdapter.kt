package com.example.joeca.kryptostats.Adapters

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.transition.ChangeBounds
import android.transition.TransitionManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.joeca.kryptostats.Activities.CurrencyResponse
import com.example.joeca.kryptostats.Models.CoinModel
import com.example.joeca.kryptostats.R
import com.example.joeca.kryptostats.Activities.SingleCoinActivity
import com.example.joeca.kryptostats.Models.CryptoCompareCoinPriceResponse
import com.example.joeca.kryptostats.Network.CryptocompareApi
import com.example.joeca.kryptostats.R.id.coinDetails
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
    val coinPrice = itemView.coinPrice
    val coinSummary = itemView.coinSummary
    val coinDetails = itemView.coinDetails
    val coinAlgorithm = itemView.coinLayoutAlgorithm
    val coinRank = itemView.coinLayoutRank
    val coinPow = itemView.coinLayoutPoW
}

class CoinItemAdapter(var recyclerView: RecyclerView, internal var activity: Activity, var items: CoinMap, val cryptoCompareApi: CryptocompareApi) : RecyclerView.Adapter<CoinItemViewHolder>() {
    var itemsList = toList()
    var isLoaded = false
    var mExpandedPosition = -1
    var previousExpandedPosition = -1

    private fun toList(): MutableList<Pair<String, CoinModel>> {
        return items
                .toList()
                .sortedWith(compareBy({ it.second.SortOrder }))
                .toMutableList()
    }

    fun setIsLoaded(loaded: Boolean) {
        this.isLoaded = loaded
    }

    fun updateData(coinItems: Map<String, CoinModel>) {
        this.items = coinItems
        itemsList = toList()
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: CoinItemViewHolder?, position: Int) {
        val coinModel = itemsList[position].second
        val isExpanded = position == mExpandedPosition
        val item = holder as CoinItemViewHolder

        item.coinDetails.visibility = if (isExpanded) View.VISIBLE else View.GONE
        item.coinSymbol.text = coinModel.Name
        item.coinFullName.text = coinModel.CoinName
        item.coinAlgorithm.text = coinModel.Algorithm
        item.coinRank.text = coinModel.SortOrder.toString()
        item.coinPow.text = coinModel.ProofType

        if (isExpanded) {
            previousExpandedPosition = position
            item.coinPrice.text = coinModel.Price.toString()
        }

        Picasso.get()
                .load("https://www.cryptocompare.com${coinModel.ImageUrl}")
                .into(item.coinIcon)

        item.itemView.setOnClickListener({ _ ->
            val transition = ChangeBounds()
            transition.setDuration(200)
            mExpandedPosition = if (isExpanded) -1 else position

            TransitionManager.beginDelayedTransition(recyclerView, transition)
            notifyItemChanged(position)
            if (isExpanded) return@setOnClickListener

            cryptoCompareApi.coinPrice(coinModel.Name).enqueue(object : Callback {
                override fun onFailure(call: Call?, e: IOException?) {
                    Toast.makeText(activity.applicationContext, "Oops, Failed to fetch Currencies", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call?, response: Response?) {
                    var price: Double

                    if (response?.isSuccessful == true) {
                        val responseJson = CryptoCompareCoinPriceResponse(response)
                        price = responseJson.prices["USD"]!!
                    } else {
                        price = -1.0
                    }

                    coinModel.Price = price
                    activity.runOnUiThread {
                        notifyDataSetChanged()
                    }
                }
            })
        })
    }

    fun setDetailsPrice(price: String, itemView: CoinItemViewHolder, position: Int) {
        println(price)
        activity.runOnUiThread {
            itemView.coinPrice.text = price
            notifyItemChanged(position)
            notifyItemChanged(previousExpandedPosition)
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