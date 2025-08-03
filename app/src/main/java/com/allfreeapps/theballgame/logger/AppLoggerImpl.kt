package com.allfreeapps.theballgame.util // Or your preferred package

import android.content.Context
import android.util.Log
import com.allfreeapps.theballgame.BuildConfig // Or your specific flag for enabling this logger
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


class AppLoggerImpl : Applogger {

    companion object {
        private const val MAX_LOG_SIZE_BYTES = 1 * 1024 * 1024 // 1 MB
        private const val LOG_FILE_NAME = "app_log.txt"
        private const val TAG = "Applogger"
    }

    private var logWriter: FileWriter? = null
    private var currentLogFile: File? = null
    private var isInitialized = false
    private lateinit var appContext: Context // Store context after initialization

    override fun initialize(context: Context) {
        if (isInitialized) {
            Log.w(TAG, "Logger already initialized.")
            return
        }
        this.appContext =
            context.applicationContext // Use applicatiFFapp_logOOon context to avoid leaks

        if (BuildConfig.DEBUG) {
            Log.i(TAG, "Debug build detected, Applogger file logging will not be initialized.")
        }

        try {
            val logDir = File(this.appContext.filesDir, "logs")
            if (!logDir.exists()) {
                logDir.mkdirs()
            }
            currentLogFile = File(logDir, LOG_FILE_NAME)

            if (currentLogFile!!.exists() && currentLogFile!!.length() > MAX_LOG_SIZE_BYTES) {
                val archiveFile =
                    File(logDir, "${LOG_FILE_NAME}_old_${System.currentTimeMillis()}.txt")
                currentLogFile!!.renameTo(archiveFile)
                currentLogFile = File(logDir, LOG_FILE_NAME)
            }

            logWriter = FileWriter(currentLogFile!!, true) // Append mode
            isInitialized = true
            i("ReleaseLogger initialized. App Version: ${getAppVersion(this.appContext)}")
        } catch (e: IOException) {
            Log.e(TAG, "Failed to initialize file logger", e)
            isInitialized = false // Ensure it's marked as not initialized
        }
    }

    private fun getAppVersion(context: Context): String {
        return try {
            val packageInfo = context.packageManager.getPackageInfo(context.packageName, 0)
            "${packageInfo.versionName} (${packageInfo.longVersionCode})"
        } catch (e: Exception) {
            "Unknown"
        }
    }

    override fun i(message: String, tag: String) {
        writeToLog("I", tag, message)
    }

    override fun w(message: String, tag: String) {
        writeToLog("W", tag, message)
    }

    override fun e(message: String, tag: String, throwable: Throwable?) {
        val fullMessage = if (throwable != null) {
            "$message\n${Log.getStackTraceString(throwable)}"
        } else {
            message
        }
        writeToLog("E", tag, fullMessage)
    }

    private fun writeToLog(level: String, tag: String, message: String) {
        if (!isInitialized || logWriter == null) {
            Log.w(
                TAG,
                "Logger not initialized or writer is null. Message lost: [$level/$tag] $message"
            )
            // Optionally, you could try to re-initialize or queue messages, but that adds complexity.
            return
        }

        // Also log to Logcat for visibility during development/testing of this logger
        if (BuildConfig.DEBUG) {
            when (level) {
                "I" -> Log.i(tag, "[FILE_LOG] $message")
                "W" -> Log.w(tag, "[FILE_LOG] $message")
                "E" -> Log.e(tag, "[FILE_LOG] $message")
                else -> Log.d(tag, "[FILE_LOG] $message")
            }
        }

        logWriter?.let {
            try {
                val timestamp =
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault()).format(Date())
                it.append("$timestamp $level/$tag: $message\n")
                it.flush()
            } catch (e: IOException) {
                Log.e(TAG, "Error writing to log file", e)
            }
        }
    }

    override fun getLogFile(): File? {
        return if (isInitialized) currentLogFile else null
    }

    override fun close() {
        if (!isInitialized) return
        try {
            logWriter?.flush()
            logWriter?.close()
        } catch (e: IOException) {
            Log.e(TAG, "Error closing log writer", e)
        } finally {
            logWriter = null
            isInitialized = false
            Log.i(TAG, "AppLogger closed.")
        }
    }
}