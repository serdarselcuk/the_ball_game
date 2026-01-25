package com.allfreeapps.theballgame.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.OptIn
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.core.content.FileProvider
import androidx.media3.common.util.Log
import androidx.media3.common.util.UnstableApi
import com.allfreeapps.theballgame.ui.theme.GameColorScale
import com.allfreeapps.theballgame.ui.theme.reflectingSunLightColorOnTheBall
import java.io.File

fun Int.toBallColor(): Color {
    val num = this % 10 // to remove markers from the ball color
    return GameColorScale[num]
}


/**
 * Creates a radial gradient brush for the ball's appearance.
 * This is a utility function, not a composable.
 */
fun Density.getRadialGradientBrush(
    animatedSize: Dp = 1.dp,
    baseColor: Color
): Brush {

    val size = if (animatedSize == 0.dp) 0.1f else with(this) {
        animatedSize.toPx()
    }

    val edgeColor = reflectingSunLightColorOnTheBall.copy(
        red = (baseColor.red * 0.5f).coerceAtMost(1f),
        green = (baseColor.green * 0.5f).coerceAtMost(1f),
        blue = (baseColor.blue * 0.5f).coerceAtMost(1f)
    )
    val centerColor = reflectingSunLightColorOnTheBall.copy(
        red = (baseColor.red * 5f).coerceAtMost(1f),
        green = (baseColor.green * 5f).coerceAtMost(1f),
        blue = (baseColor.blue * 5f).coerceAtMost(1f)
    )
    return Brush.radialGradient(
        colors = listOf(
            centerColor,
            baseColor,
            edgeColor
        ),
        radius = size * 2f,
        center = Offset(
            x = size * 0.1f,
            y = size * 0.1f
        ),
        tileMode = TileMode.Mirror
    )
}

@OptIn(UnstableApi::class)
fun shareLogFile(context: Context, logFile: File?) {
    if (logFile == null || !logFile.exists()) {
        Log.e("ShareLog", "Log file is null or does not exist.")
        // Show a message to the user
        return
    }

    val logFileUri: Uri
    try {
        // Use FileProvider for secure sharing, especially on Android N (API 24) and above
        // You'll need to configure FileProvider in your AndroidManifest.xml
        // and create a paths.xml file.
        logFileUri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider", // Authority matches your manifest
            logFile
        )
    } catch (e: IllegalArgumentException) {
        Log.e("ShareLog", "FileProvider error: ${e.message}")
        // Show an error message to the user
        return
    }

    val shareIntent = Intent(Intent.ACTION_SEND).apply {
        type = "text/plain" // Or "application/octet-stream" for a generic file
        putExtra(Intent.EXTRA_STREAM, logFileUri)
        putExtra(Intent.EXTRA_SUBJECT, "App Log File")
        putExtra(Intent.EXTRA_TEXT, "Please find the attached app log file.")
        addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // Grant read permission to the receiving app
    }

    // Verify that there's an app available to handle this intent
    if (shareIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(Intent.createChooser(shareIntent, "Share Log File Via"))
    } else {
        Log.e("ShareLog", "No app found to handle sharing.")
        // Show a message to the user
    }
}
