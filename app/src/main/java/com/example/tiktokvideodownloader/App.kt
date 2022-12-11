package com.example.tiktokvideodownloader

import android.app.Application
import com.example.tiktokvideodownloader.di.ServiceLocator

class App : Application()  {

    override fun onCreate() {
        super.onCreate()
        ServiceLocator.start(this)
    }
}