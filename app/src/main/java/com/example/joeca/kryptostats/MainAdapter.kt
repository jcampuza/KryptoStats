package com.example.joeca.kryptostats

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import kotlinx.android.synthetic.main.currency_row.view.*

/**
 * Created by joeca on 2/15/2018.
 */


typealias TickerMap = Map<String, Ticker>

class MainAdapter(val tickers: TickerMap, val context: Context): RecyclerView.Adapter<MainAdapter.ViewHolder>() {

    var tickerList = cloneTickers()

    fun cloneTickers(): MutableList<Pair<String, Ticker>> {
        return tickers.toList().toMutableList()
    }

    override fun getItemCount(): Int {
        println(tickers.count())
        return tickerList.size
    }

    fun filter(text: String) {
        tickerList.clear()
        if (text.isEmpty()) {
            tickerList = cloneTickers()
        } else {
            val lowerCaseText = text.toLowerCase()
            for ((key, value) in tickers) {
                val containsText =
                        value.Name.toLowerCase().contains(lowerCaseText) ||
                        value.CoinName.toLowerCase().contains(lowerCaseText)

                if (containsText) {
                    tickerList.add(Pair(key, value))
                }
            }
        }

        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent?.context)
        val cellForRow = layoutInflater.inflate(R.layout.currency_row, parent, false)

        return ViewHolder(cellForRow)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val coin = tickerList[position]
        holder.ticker.text = coin.first
        holder.price.text = coin.second.CoinName

        holder.itemView.setOnClickListener {
            Toast.makeText(context, coin.second.Name, Toast.LENGTH_LONG)
            val context = holder.itemView.context
            val intent: Intent = Intent(context, SingleCoinActivity::class.java)
            val extras = Bundle();
            extras.putString("ticker", coin.first)
            extras.putString("name", coin.second.Name)
            extras.putString("url", coin.second.Url)
            intent.putExtras(extras)

            context.startActivity(intent)
        }
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val ticker = view.textView_currency_ticker
        val price = view.textView_currency_price
    }
}