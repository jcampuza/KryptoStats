package com.example.joeca.kryptostats.Activities

import android.graphics.Color
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.example.joeca.kryptostats.Adapters.CoinItemAdapter
import com.example.joeca.kryptostats.Application.KryptoApplication
import com.example.joeca.kryptostats.Models.CoinModel
import com.example.joeca.kryptostats.Models.CryptoCompareCoinListResponse
import com.example.joeca.kryptostats.R
import com.example.joeca.kryptostats.Network.CryptocompareApi
import com.google.gson.GsonBuilder
import com.miguelcatalan.materialsearchview.MaterialSearchView
import kotlinx.android.synthetic.*
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.*
import java.io.IOException
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var cryptoCompareApi: CryptocompareApi

    lateinit var adapter: CoinItemAdapter
    lateinit var materialSearchView: MaterialSearchView
    var items = emptyMap<String, CoinModel>()
    var searchQuery: String = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        (application as KryptoApplication).kryptoComponent.inject(this)

        setupMaterialSearchMenu()
        recyclerView_main.layoutManager = LinearLayoutManager(this)
        setupCoinItemAdapter()
        fetchCoinList()
    }

    fun setupMaterialSearchMenu() {
        val toolbar = findViewById<android.support.v7.widget.Toolbar>(R.id.toolbarMain)
        setSupportActionBar(toolbar)
        supportActionBar?.title = "Search..."
        toolbar.setTitleTextColor(Color.parseColor("#FFFFFF"))
        materialSearchView = findViewById(R.id.search_view)

        materialSearchView.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewClosed() {}

            override fun onSearchViewShown() {
                println("Showing Search View")
                println(searchQuery)
                materialSearchView.setQuery(searchQuery, false)
            }
        })

        materialSearchView.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText!= null && !newText.isEmpty()) {
                    searchQuery = newText
                    var filteredItems = mutableMapOf<String, CoinModel>()
                    var formattedQuery = newText.toLowerCase()

                    for ((key, value) in items) {
                        var containsName = value.Name.toLowerCase().contains(formattedQuery)
                        var containsCoinName = value.CoinName.toLowerCase().contains(formattedQuery)

                        if (containsName or containsCoinName) {
                            filteredItems[key] = value
                        }
                    }

                    adapter.updateData(filteredItems)
                } else {
                    searchQuery = ""
                    adapter.updateData(items)
                }
                return true
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.search_menu, menu)
        val item: MenuItem = menu.findItem(R.id.search_action)
        materialSearchView.setMenuItem(item)

        return true
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
                items = responseJson.Data

                runOnUiThread {
                    adapter.setIsLoaded(true)
                    adapter.updateData(items)
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
