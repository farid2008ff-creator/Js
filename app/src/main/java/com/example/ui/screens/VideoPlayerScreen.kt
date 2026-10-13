package com.example.ui.screens

import android.view.KeyEvent
import android.view.ViewGroup
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AspectRatio
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.ui.AspectRatioFrameLayout
import androidx.media3.ui.PlayerView
import com.example.data.model.TvChannel
import com.example.data.repository.ChannelRepository
import com.example.player.PlayerViewModel
import com.example.ui.components.LiveIndicatorBadge
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.LiveRed
import com.example.ui.theme.RedPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun VideoPlayerScreen(
    playerViewModel: PlayerViewModel,
    channel: TvChannel,
    isTv: Boolean,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val currentChannel by playerViewModel.currentChannel.collectAsState()
    val isPlaying by playerViewModel.isPlaying.collectAsState()
    val isBuffering by playerViewModel.isBuffering.collectAsState()
    val errorMessage by playerViewModel.errorMessage.collectAsState()
    val streamQuality by playerViewModel.streamQuality.collectAsState()
    val resizeMode by playerViewModel.resizeMode.collectAsState()
    val showControls by playerViewModel.showControls.collectAsState()

    val focusRequester = remember { FocusRequester() }

    BackHandler {
        onBack()
    }

    LaunchedEffect(channel) {
        if (currentChannel?.id != channel.id) {
            playerViewModel.playChannel(context, channel)
        }
        focusRequester.requestFocus()
    }

    val activeChannel = currentChannel ?: channel

    val resizeModeLabel = when (resizeMode) {
        AspectRatioFrameLayout.RESIZE_MODE_FIT -> "16:9 Uyğun"
        AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> "Kəsmə / Zoom"
        AspectRatioFrameLayout.RESIZE_MODE_FILL -> "Tam Doldurma"
        else -> "16:9"
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
            .testTag("video_player_screen")
            .focusRequester(focusRequester)
            .focusable()
            .onKeyEvent { keyEvent ->
                if (keyEvent.nativeKeyEvent.action == KeyEvent.ACTION_DOWN) {
                    val keyCode = keyEvent.nativeKeyEvent.keyCode
                    when (keyCode) {
                        KeyEvent.KEYCODE_DPAD_UP, KeyEvent.KEYCODE_CHANNEL_UP -> {
                            playerViewModel.prevChannel(context)
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_DOWN, KeyEvent.KEYCODE_CHANNEL_DOWN -> {
                            playerViewModel.nextChannel(context)
                            true
                        }
                        KeyEvent.KEYCODE_DPAD_CENTER, KeyEvent.KEYCODE_ENTER -> {
                            playerViewModel.toggleControls()
                            true
                        }
                        KeyEvent.KEYCODE_1 -> {
                            ChannelRepository.getChannelByNumber(1)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_2 -> {
                            ChannelRepository.getChannelByNumber(2)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_3 -> {
                            ChannelRepository.getChannelByNumber(3)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_4 -> {
                            ChannelRepository.getChannelByNumber(4)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_5 -> {
                            ChannelRepository.getChannelByNumber(5)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_6 -> {
                            ChannelRepository.getChannelByNumber(6)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_7 -> {
                            ChannelRepository.getChannelByNumber(7)?.let { playerViewModel.playChannel(context, it) }
                            true
                        }
                        KeyEvent.KEYCODE_BACK -> {
                            onBack()
                            true
                        }
                        else -> false
                    }
                } else false
            }
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                playerViewModel.toggleControls()
            }
    ) {
        // Android MediaPlayer / ExoPlayer view
        AndroidView(
            factory = { ctx ->
                PlayerView(ctx).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    useController = false
                    this.player = playerViewModel.getPlayer(ctx)
                    this.resizeMode = resizeMode
                    setBackgroundColor(android.graphics.Color.BLACK)
                }
            },
            update = { playerView ->
                playerView.player = playerViewModel.getPlayer(context)
                playerView.resizeMode = resizeMode
            },
            modifier = Modifier.fillMaxSize()
        )

        // Buffering State Center
        if (isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(54.dp),
                        color = CyanGlow,
                        strokeWidth = 4.dp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "${activeChannel.name} Yayımı Yüklənir…",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = streamQuality,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        // Error State Center
        if (errorMessage != null && !isBuffering) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black.copy(alpha = 0.75f)),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(24.dp)
                        .background(DarkSurface, RoundedCornerShape(16.dp))
                        .border(1.dp, LiveRed.copy(alpha = 0.5f), RoundedCornerShape(16.dp))
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        imageVector = Icons.Default.Warning,
                        contentDescription = "Xəta",
                        tint = LiveRed,
                        modifier = Modifier.size(42.dp)
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = "Yayım Açıla Bilmədi",
                        color = Color.White,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = errorMessage ?: "Yayım bağlantısı kəsildi və ya token yenilənməlidir.",
                        color = TextSecondary,
                        fontSize = 12.sp,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                        Button(
                            onClick = { playerViewModel.retry(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = RedPrimary),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Yenidən Cəhd Et")
                        }
                        Button(
                            onClick = { playerViewModel.nextChannel(context) },
                            colors = ButtonDefaults.buttonColors(containerColor = DarkBorder),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("Növbəti Kanal")
                        }
                    }
                }
            }
        }

        // Controls Overlay (Animated Fade & Slide)
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically { -it / 2 },
            exit = fadeOut() + slideOutVertically { -it / 2 },
            modifier = Modifier.fillMaxWidth().align(Alignment.TopCenter)
        ) {
            // Top Bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Black.copy(alpha = 0.85f), Color.Transparent)
                        )
                    )
                    .padding(horizontal = 16.dp, vertical = 12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        IconButton(
                            onClick = onBack,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                                .testTag("player_back_button")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = "Geri",
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "${String.format("%02d", activeChannel.channelNumber)} • ${activeChannel.name}",
                                    color = Color.White,
                                    fontSize = 17.sp,
                                    fontWeight = FontWeight.Bold
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LiveIndicatorBadge()
                            }
                            Text(
                                text = "${activeChannel.category}  |  $streamQuality",
                                color = TextSecondary,
                                fontSize = 11.sp
                            )
                        }
                    }

                    // Top Action Controls
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Aspect Ratio cycle button
                        IconButton(
                            onClick = { playerViewModel.cycleResizeMode() },
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color.White.copy(alpha = 0.12f))
                                .padding(horizontal = 4.dp)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 6.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AspectRatio,
                                    contentDescription = "Ölçü",
                                    tint = CyanGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = resizeModeLabel,
                                    color = Color.White,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        // Refresh / Reload stream
                        IconButton(
                            onClick = { playerViewModel.retry(context) },
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(Color.White.copy(alpha = 0.12f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Refresh,
                                contentDescription = "Yenilə",
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }
        }

        // Center Play / Pause button when controls are visible
        if (showControls && !isBuffering && errorMessage == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                IconButton(
                    onClick = { playerViewModel.togglePlayPause() },
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(Color.Black.copy(alpha = 0.6f))
                        .border(2.dp, CyanGlow.copy(alpha = 0.8f), CircleShape)
                        .testTag("play_pause_button")
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pauza" else "Oynat",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
            }
        }

        // Bottom Controls: Channel Navigation Bar & Quick Channel Drawer
        AnimatedVisibility(
            visible = showControls,
            enter = fadeIn() + slideInVertically { it / 2 },
            exit = fadeOut() + slideOutVertically { it / 2 },
            modifier = Modifier.fillMaxWidth().align(Alignment.BottomCenter)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f))
                        )
                    )
                    .padding(horizontal = 14.dp, vertical = 12.dp)
            ) {
                // Navigation Prev / Channel Tray / Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Prev Channel Button
                    IconButton(
                        onClick = { playerViewModel.prevChannel(context) },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .testTag("prev_channel_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipPrevious,
                            contentDescription = "Əvvəlki Kanal",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Horizontal Quick Channel Selector
                    LazyRow(
                        modifier = Modifier.weight(1f),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(ChannelRepository.channels, key = { it.id }) { ch ->
                            val isSelected = ch.id == activeChannel.id
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        if (isSelected) RedPrimary else DarkSurface.copy(alpha = 0.85f)
                                    )
                                    .border(
                                        width = if (isSelected) 1.5.dp else 1.dp,
                                        color = if (isSelected) Color.White else DarkBorder,
                                        shape = RoundedCornerShape(10.dp)
                                    )
                                    .clickable {
                                        playerViewModel.playChannel(context, ch)
                                    }
                                    .padding(horizontal = 12.dp, vertical = 7.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${ch.channelNumber}",
                                        color = if (isSelected) Color.White else CyanGlow,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Black
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = ch.name,
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    // Next Channel Button
                    IconButton(
                        onClick = { playerViewModel.nextChannel(context) },
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.15f))
                            .testTag("next_channel_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.SkipNext,
                            contentDescription = "Növbəti Kanal",
                            tint = Color.White,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                }
            }
        }
    }
}
