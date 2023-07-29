package com.ident.main.mngtapp

import androidx.multidex.MultiDexApplication

class IDentApplication : MultiDexApplication() {
    companion object {
        @get:Synchronized
        lateinit var instance: IDentApplication

    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}