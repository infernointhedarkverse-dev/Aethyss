package com.aethyss

import android.app.Application
import android.util.Log

class AethyssApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Log.d("AethyssApp", "Aethyss application initialized")
    }
}

