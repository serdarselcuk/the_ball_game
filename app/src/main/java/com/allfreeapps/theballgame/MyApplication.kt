package com.allfreeapps.theballgame

import android.app.Application
import androidx.activity.result.launch
import com.allfreeapps.theballgame.service.AppDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class MyApplication: Application() {
    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main) // Or IO if you prefer for this specific task

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