package com.example.localchat

import android.app.Application
import android.content.Context
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.localchat.data.AppDataManager


class MyApplication : Application(), LifecycleObserver {
    init {
       myApplication = this
    }

    companion object {
        private lateinit var myApplication: Application
        fun getApplicationContext(): Context {
            return myApplication
        }
    }

    override fun onCreate() {
        super.onCreate()
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onEnterForeground() {
        AppDataManager.getMyInstance().setApplicationOnStatus(true)
    }

    /**
     * Shows background
     */
    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onEnterBackground() {
        AppDataManager.getMyInstance().setApplicationOnStatus(false)
    }

}