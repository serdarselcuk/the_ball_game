package com.allfreeapps.theballgame.utils

// Your other imports like:
import android.content.Context
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.allfreeapps.theballgame.R
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

// ... other necessary imports

enum class SoundType(@RawRes val resourceId: Int) {
    DEFAULT_TAP(R.raw.tap_voice),
    BUBBLE_EXPLODE(R.raw.bubble_explode),
    BUTTON_CLICK(R.raw.click),
    // Add more sound types as needed
}

object SoundPlayerManager {
    private val players = mutableMapOf<SoundType, ExoPlayer>()
    // To manage multiple instances for overlapping sounds
    private val soundInstancePool = mutableMapOf<SoundType, MutableList<ExoPlayer>>()
    private const val MAX_INSTANCES_PER_SOUND = 3 // Max parallel instances for a single sound type
    private const val OVERLAP_DELAY_MS = 200L // Delay for overlapping sounds
    private var isInitialized = false // This flag might need rethinking if players can be individually released/re-initialized

    // Consider if you still want a global initialize for all,
    // or initialize on demand. For this example, let's assume
    // you might initialize them individually or all at once.

    fun initializeSound(context: Context, soundType: SoundType) {
        if (soundInstancePool.containsKey(soundType) && (soundInstancePool[soundType]?.size ?: 0) > 0) {
            // Optionally, release the old one if it exists and you want to re-initialize
            // players[soundType]?.release()
            // return // Or allow re-initialization
        }

        soundInstancePool.putIfAbsent(soundType, mutableListOf())
        val instances = soundInstancePool[soundType]!!

        // Create a pool of players for each sound type
        // For simplicity, we'll still keep one primary "player" for direct access if needed,
        // but the pool will be used for overlapping plays.
        if (!players.containsKey(soundType)) {
            val mainPlayer = createPlayer(context, soundType)
            players[soundType] = mainPlayer
            instances.add(mainPlayer) // Add the main player to the pool as well
        }

        // Pre-populate the pool up to MAX_INSTANCES_PER_SOUND
        while (instances.size < MAX_INSTANCES_PER_SOUND) {
            instances.add(createPlayer(context, soundType))
        }
        isInitialized = true
    }

    private fun createPlayer(context: Context, soundType: SoundType): ExoPlayer {
        return ExoPlayer.Builder(context.applicationContext).build().apply {
            val mediaItem = MediaItem.fromUri("android.resource://${context.applicationContext.packageName}/${soundType.resourceId}")
            setMediaItem(mediaItem)
            playWhenReady = false // Important: Do not auto-play on prepare
            repeatMode = Player.REPEAT_MODE_OFF
            prepare()
        }
    }

    // Optional: Keep the global initialize if you want to load all common sounds at once
    fun initializeAll(context: Context) {
        if (isInitialized && players.size == SoundType.entries.size &&
            soundInstancePool.all { it.value.size >= MAX_INSTANCES_PER_SOUND }) return // Basic check

        SoundType.entries.forEach { soundType ->
            // Ensure each sound type has its pool initialized
            if (!soundInstancePool.containsKey(soundType) || (soundInstancePool[soundType]?.size ?: 0) < MAX_INSTANCES_PER_SOUND) {
                initializeSound(context, soundType)
            }
        }
        isInitialized = true // Or set based on whether any player was initialized
    }


    fun playSound(soundType: SoundType, context: Context? = null) { // Pass context if you want to initialize on demand
        val player = players[soundType]
        val instancePool = soundInstancePool[soundType]

        if (player == null && context != null) {
            // Option: Initialize on demand if not found
            initializeSound(context, soundType)
            // After initialization, try to play again (recursive call or just proceed)
            playSoundInternal(soundType, soundInstancePool[soundType])
            return
        }

        playSoundInternal(soundType, instancePool)
    }

    private val coroutineScope = CoroutineScope(Dispatchers.Main + SupervisorJob())
    private val activePlayJobs = mutableMapOf<SoundType, MutableList<Job>>()

    private fun playSoundInternal(soundType: SoundType, instances: MutableList<ExoPlayer>?) {
        if (instances.isNullOrEmpty()) return

        // Find an available player instance (not currently playing)
        var playerToUse: ExoPlayer? = instances.find { !it.isPlaying }

        // If all instances are busy, and we allow overlapping, pick one (e.g., the first one)
        // and it will restart. Or, if you want a slight delay for noticeable overlap:
        if (playerToUse == null && instances.isNotEmpty()) {
            // All players in the pool are busy. We'll queue this request with a delay.
            val job = coroutineScope.launch {
                delay(OVERLAP_DELAY_MS * (activePlayJobs[soundType]?.count { it.isActive } ?: 0))
                // Re-attempt to find an available player after delay
                val delayedPlayer = instances.find { !it.isPlaying } ?: instances.first() // Fallback to first if still all busy
                delayedPlayer.seekTo(0)
                delayedPlayer.play()
            }
            activePlayJobs.getOrPut(soundType) { mutableListOf() }.add(job)
            job.invokeOnCompletion { activePlayJobs[soundType]?.remove(job) }
            return
        }

        playerToUse?.seekTo(0)
        playerToUse?.play()
    }

    fun releaseSound(soundType: SoundType) {
        players.remove(soundType)?.release()
        soundInstancePool.remove(soundType)?.forEach { it.release() }
        activePlayJobs.remove(soundType)?.forEach { it.cancel() }
        if (players.isEmpty()) {
            isInitialized = false
        }
    }

    fun releaseAll() { // Renamed from release to be more explicit
        players.values.forEach { it.release() }
        players.clear()
        soundInstancePool.values.forEach { list -> list.forEach { it.release() } }
        soundInstancePool.clear()
        activePlayJobs.values.forEach { list -> list.forEach { it.cancel() } }
        activePlayJobs.clear()
        coroutineScope.cancel() // Cancel all coroutines when SoundPlayerManager is no longer needed
        isInitialized = false
    }
}