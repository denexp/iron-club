package com.denisdev.ironclub

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class IronClubApplication: Application() {

    override fun onCreate() {
        super.onCreate()
    }
}