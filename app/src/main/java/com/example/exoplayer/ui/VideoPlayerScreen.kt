package com.example.exoplayer.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.exoplayer.viewModel.PlayerViewModel

// Pantalla de reproduccion
@Composable
fun VideoPlayerScreen(videoPath: Int, playerViewModel: PlayerViewModel = viewModel()) {
    val context = LocalContext.current
    // Estado desde el vm
    val player = playerViewModel.playerState.collectAsState().value
    val resolution by playerViewModel.videoResolution.collectAsState()

    // Prepara el video
    LaunchedEffect(videoPath) {
        playerViewModel.initializePlayer(context, videoPath)
    }

    // Libera el reproductor
    DisposableEffect(Unit) {
        onDispose {
            playerViewModel.releasePlayer()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Media3AndroidView(player)

        // Texto de resolución flotante
        Text(
            text = "Resolución: ${resolution.first} x ${resolution.second}",
            textAlign = TextAlign.Center,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .background(Color.DarkGray)
                .padding(8.dp),
            fontSize = 16.sp,
            color = Color.White
        )
    }
}
