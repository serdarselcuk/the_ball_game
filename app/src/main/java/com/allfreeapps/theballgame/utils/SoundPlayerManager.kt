package com.allfreeapps.theballgame.utils

import android.content.Context
import androidx.annotation.RawRes
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.allfreeapps.theballgame.R
import com.allfreeapps.theballgame.service.SettingsRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

enum class SoundType( @RawRes val resourceId: Int) {

    DEFAULT_TAP(R.raw.empty_tap),
    BUBBLE_EXPLODE(R.raw.bubble_explode),
    EMPTY_TAP(R.raw.filled_tap),
    FILLED_TAP(R.raw.bubble),
    HISS(R.raw.hissing),
}

@Singleton
class SoundPlayerManager @Inject constructor(
    @ApplicationContext private val context: Context,
    private val settingsRepository: SettingsRepository
) {
    private val players = mutableMapOf<SoundType, ExoPlayer>()

    private fun initialize(soundType: SoundType): Player {
        return players.getOrPut(soundType) {
            createAndConfigurePlayer(soundType)
        }
    }

    private fun createAndConfigurePlayer(soundType: SoundType): ExoPlayer {
        return ExoPlayer.Builder(context.applicationContext)
            .build()
            .apply {
                val mediaItem = MediaItem.fromUri(
                    "android.resource://${context.applicationContext.packageName}/${soundType.resourceId}"
                )
                setMediaItem(mediaItem)
                playWhenReady = false // Don't play immediately
                repeatMode = Player.REPEAT_MODE_OFF
                volume = settingsRepository.getVolume(soundType).percentage * settingsRepository.getMasterVolume().percentage
                prepare()
            }
    }

    fun playSound(soundType: SoundType = SoundType.DEFAULT_TAP) {

        (players[soundType] ?: initialize(soundType))
         .apply {
            seekTo(0)
            play()
        }

    }

    fun releaseAll() {
        players.values.forEach { it.release() }
        players.clear()
    }

    // this overLoaded type is for only changing the volume of a specific sound
    fun updateVolume(soundType: SoundType, newVolume: Int) {
        updateVolume(soundType, newVolume.percentage * settingsRepository.getMasterVolume().percentage)
    }

    // this can be called if only sound is in float type and private for the class
    // since it has to be calculated with master ans particular voice levels
    private fun updateVolume(soundType: SoundType, newVolume: Float) {
        players[soundType]?.volume = newVolume
    }

    fun updateAllVolumes(masterVolume: Int) {
        SoundType.entries.forEach {
            updateVolume(it,
                settingsRepository.getVolume(it).percentage * masterVolume.percentage
                )
        }
    }

    private val Int.percentage: Float get() = this/100F

}
