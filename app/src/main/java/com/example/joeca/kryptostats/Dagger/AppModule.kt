package com.example.joeca.kryptostats.Dagger

import android.app.Application
import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

/**
 * Created by campu on 4/9/2018.
 */

@Module
class AppModule(private val app: Application) {
  @Provides
  @Singleton
  fun provideContext(): Context = app
}