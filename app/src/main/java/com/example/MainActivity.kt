package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.data.model.TvChannel
import com.example.player.PlayerViewModel
import com.example.ui.screens.ChannelListScreen
import com.example.ui.screens.VideoPlayerScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.util.DeviceUtils

class MainActivity : ComponentActivity() {

    private val playerViewModel: PlayerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MyApplicationTheme {
                CanliTvApp(
                    activity = this,
                    playerViewModel = playerViewModel
                )
            }
        }
    }

    override fun onStop() {
        super.onStop()
        // Pause player when activity goes to background
        playerViewModel.getPlayer(this).pause()
    }
}

@Composable
fun CanliTvApp(
    activity: MainActivity,
    playerViewModel: PlayerViewModel = viewModel()
) {
    val isTv = remember { DeviceUtils.isTv(activity) }
    var selectedChannel by remember { mutableStateOf<TvChannel?>(null) }

    DisposableEffect(selectedChannel) {
        if (selectedChannel != null) {
            // Auto landscape and auto fullscreen for phones; auto fullscreen for TVs
            DeviceUtils.enterPlayerFullscreen(activity, isTv)
        } else {
            DeviceUtils.exitPlayerFullscreen(activity, isTv)
        }
        onDispose { }
    }

    val current = selectedChannel
    if (current != null) {
        VideoPlayerScreen(
            playerViewModel = playerViewModel,
            channel = current,
            isTv = isTv,
            onBack = {
                playerViewModel.stopPlayback()
                selectedChannel = null
            },
            modifier = Modifier.fillMaxSize()
        )
    } else {
        ChannelListScreen(
            isTv = isTv,
            onChannelSelected = { channel ->
                selectedChannel = channel
            },
            modifier = Modifier.fillMaxSize()
        )
    }
}
