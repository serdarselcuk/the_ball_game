package com.allfreeapps.theballgame.utils

// Your other imports like:
import android.content.Context
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.allfreeapps.theballgame.R

// ... other necessary imports

enum class SoundType(@RawRes val resourceId: Int, val volume: Float =  1.0f) {
    DEFAULT_TAP(R.raw.empty_tap),
    BUBBLE_EXPLODE(R.raw.bubble_explode),
    EMPTY_TAP(R.raw.filled_tap),
    FILLED_TAP(R.raw.bubble),
    HISS(R.raw.hissing, 0.5f),
    // Add more sound types as needed
}
object SoundPlayerManager {
    private val players = mutableMapOf<SoundType, ExoPlayer>()
    private var isInitialized = false

    fun initialize(context: Context) {
        if (isInitialized) return

        SoundType.entries.forEach { soundType ->
            val player = ExoPlayer.Builder(context.applicationContext).build()
                .apply {
                    val mediaItem = MediaItem.fromUri(
                        "android.resource://${context.applicationContext.packageName}/${soundType.resourceId}"
                    )
                    setMediaItem(mediaItem)
                    playWhenReady = false // Don't play immediately
                    repeatMode = Player.REPEAT_MODE_OFF
                    volume = soundType.volume
                    prepare() // Prepare the player
                }
            players[soundType] = player
        }
        isInitialized = true
    }

    fun playSound(soundType: SoundType = SoundType.DEFAULT_TAP) {
        players[soundType]?.apply {
            seekTo(0)
            play()
        }
    }

    fun release() {
        players.values.forEach { it.release() }
        players.clear()
        isInitialized = false
        // Looper.myLooper()?.quitSafely() // Consider if you manually prepared a Looper
    }
}
