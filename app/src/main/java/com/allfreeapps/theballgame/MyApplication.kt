package com.allfreeapps.theballgame

import android.app.Application
import com.allfreeapps.theballgame.service.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

//@HiltAndroidApp
class MyApplication: Application() {
//    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    val database: AppDatabase by lazy {
        AppDatabase.getInstance(applicationContext)
    }

    override fun onCreate() {
        super.onCreate()

        CoroutineScope(Dispatchers.IO).launch {
            AppDatabase.getInstance(applicationContext)
        }
    }
}