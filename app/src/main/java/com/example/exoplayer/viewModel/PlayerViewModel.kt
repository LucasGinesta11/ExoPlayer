package com.example.exoplayer.viewModel

import android.content.Context
import android.net.Uri
import androidx.annotation.OptIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.TrackSelectionParameters
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.trackselection.DefaultTrackSelector
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

@Suppress("DEPRECATION")
class PlayerViewModel : ViewModel() {
    private val _playerState = MutableStateFlow<ExoPlayer?>(null)
    val playerState: StateFlow<ExoPlayer?> = _playerState

    @OptIn(UnstableApi::class)
    fun initializePlayer(context: Context, videoPath: Int) {
        if (_playerState.value == null) {
            viewModelScope.launch {
                val trackSelector = DefaultTrackSelector(context)

                // Establecer parámetros para forzar la selección de pistas de 3840x2160
                val parameters = TrackSelectionParameters.Builder(context)
                    .setMaxVideoSize(3840, 2160) // Tamaño máximo
                    .setMaxVideoBitrate(40000000) // Ajusta el bitrate según tus necesidades
                    .build()

                trackSelector.setParameters(parameters)

                val uri = Uri.parse("android.resource://${context.packageName}/$videoPath")
                val exoPlayer = ExoPlayer.Builder(context)
                    .setTrackSelector(trackSelector)
                    .build().apply {
                        val mediaItem = MediaItem.fromUri(uri)
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = true
                    }

                _playerState.value = exoPlayer
            }
        }
    }

    fun releasePlayer() {
        _playerState.value?.release()
        _playerState.value = null
    }
}
