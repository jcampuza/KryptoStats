package com.example.joeca.kryptostats

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * Created by joeca on 2/15/2018.
 */

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        this.finish()
    }
}