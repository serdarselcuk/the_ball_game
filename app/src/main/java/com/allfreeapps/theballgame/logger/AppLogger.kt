package com.allfreeapps.theballgame.util // Or your preferred package

import java.io.File

interface Applogger {

    /**
     * Initializes the logger, setting up the log file and rotation.
     * Should typically be called once, e.g., in Application.onCreate().
     * @param context The application context, needed for file operations.
     */
    fun initialize(context: android.content.Context)

    /**
     * Logs an informational message.
     * @param message The message to log.
     * @param tag Optional tag for the log message (defaults to "AppInfo").
     */
    fun i(message: String, tag: String = "AppInfo")

    /**
     * Logs a warning message.
     * @param message The message to log.
     * @param tag Optional tag for the log message (defaults to "AppWarning").
     */
    fun w(message: String, tag: String = "AppWarning")

    /**
     * Logs an error message.
     * @param message The message to log.
     * @param tag Optional tag for the log message (defaults to "AppError").
     * @param throwable Optional throwable to include its stack trace in the log.
     */
    fun e(message: String, tag: String = "AppError", throwable: Throwable? = null)

    /**
     * Retrieves the current log file.
     * This can be used to attach the log file to a bug report.
     * @return The current log File object, or null if not initialized or not available.
     */
    fun getLogFile(): File?

    /**
     * Closes the log writer.
     * Should be called when the application is terminating or when logging is no longer needed
     * to ensure all buffered logs are flushed and resources are released.
     */
    fun close()
}