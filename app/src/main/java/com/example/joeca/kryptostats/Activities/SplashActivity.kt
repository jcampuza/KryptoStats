package com.example.joeca.kryptostats.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.example.joeca.kryptostats.Activities.MainActivity
import com.example.joeca.kryptostats.utils.start

/**
 * Created by joeca on 2/15/2018.
 */

class SplashActivity: AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivity::class.start(this, true)
    }
}