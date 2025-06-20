package com.allfreeapps.theballgame.utils

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import android.util.Log
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Vibrator @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private var lastVibration = 0L
    private val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        val vibratorManager =
            context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
        vibratorManager.defaultVibrator
    } else {
        @Suppress("DEPRECATION")
        context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    }

    fun vibrate() {
        if (vibrator.hasVibrator() && System.currentTimeMillis() - lastVibration > VIBRATION_TIMEOUT) {
            lastVibration = System.currentTimeMillis()
            val vibrationEffect: VibrationEffect =
                VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE)
            vibrator.vibrate(vibrationEffect)
            Log.d("Vibrator", "vibrating")
        } else {
            Log.d("Vibrator", "Not vibrating - last vibration was : $lastVibration")
        }
    }

    companion object {
        const val VIBRATION_TIMEOUT = 500L
    }

}