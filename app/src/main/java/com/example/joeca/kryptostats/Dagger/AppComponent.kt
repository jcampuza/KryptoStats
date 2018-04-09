package com.example.joeca.kryptostats.Dagger

import com.example.joeca.kryptostats.Activities.MainActivity
import com.example.joeca.kryptostats.Activities.SingleCoinActivity
import dagger.Component
import javax.inject.Singleton

/**
 * Created by campu on 4/9/2018.
 */

@Singleton
@Component(modules = arrayOf(AppModule::class, NetworkModule::class))
interface AppComponent {
  fun inject(target: MainActivity)
  fun inject(target: SingleCoinActivity)
}