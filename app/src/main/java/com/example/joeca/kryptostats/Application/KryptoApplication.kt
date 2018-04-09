package com.example.joeca.kryptostats.Application

import android.app.Application
import com.example.joeca.kryptostats.Dagger.AppComponent
import com.example.joeca.kryptostats.Dagger.AppModule
import com.example.joeca.kryptostats.Dagger.DaggerAppComponent

/**
 * Created by campu on 4/9/2018.
 */

class KryptoApplication: Application() {

  lateinit var kryptoComponent: AppComponent

  private fun initDagger(app: KryptoApplication): AppComponent =
      DaggerAppComponent.builder()
          .appModule(AppModule(app))
          .build()

  override fun onCreate() {
    super.onCreate()
    kryptoComponent = initDagger(this)
  }
}