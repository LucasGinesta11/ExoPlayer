package com.example.exoplayer.viewModel

import android.content.Context
import androidx.annotation.OptIn
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.common.VideoSize
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

// ViewModel que gestiona el estado de reproduccion del video
class PlayerViewModel : ViewModel() {
    // StateFlow mantiene el estado, es null hasta que se inicia el video
    private val _playerState = MutableStateFlow<ExoPlayer?>(null)

    // Reprductor disponible
    val playerState: StateFlow<ExoPlayer?> = _playerState

    // Resolucion del video
    private val _videoResolution = MutableStateFlow(Pair(0, 0))
    val videoResolution: StateFlow<Pair<Int, Int>> = _videoResolution


    @OptIn(UnstableApi::class)
    // Inicializa el reproductor
    fun initializePlayer(context: Context, videoPath: Int) {
        if (_playerState.value == null) {
            viewModelScope.launch {
                // Crea un uri que apunta al video y luego lo transforma en un objeto Uri para ser
                // utilizado por ExoPlayer
                val uri = "android.resource://${context.packageName}/$videoPath".toUri()
                // Instancia de ExoPlayer
                val exoPlayer = ExoPlayer.Builder(context)
                    .build().apply {
                        val mediaItem = MediaItem.fromUri(uri)
                        setMediaItem(mediaItem)
                        prepare()
                        playWhenReady = true

                        // Listener para el estado del video (cambio de resolucion)
                        addListener(object : Player.Listener {
                            override fun onVideoSizeChanged(videoSize: VideoSize) {
                                _videoResolution.value = Pair(videoSize.width, videoSize.height)
                            }
                        })
                    }
                _playerState.value = exoPlayer
            }
        }
    }

    // Libera los recursos
    fun releasePlayer() {
        _playerState.value?.release()
        _playerState.value = null
    }
}
