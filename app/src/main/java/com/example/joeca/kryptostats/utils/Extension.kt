package com.example.joeca.kryptostats.utils

import android.app.Activity
import android.content.Intent
import kotlin.reflect.KClass

/**
 * Created by campu on 4/9/2018.
 */

fun <T: Activity> KClass<T>.start(activity: Activity, finish: Boolean = false) {
  Intent(activity, this.java).apply {
    activity.startActivity(this)
  }

  if (finish) {
    activity.finish()
  }
}