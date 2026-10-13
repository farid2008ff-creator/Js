package com.example.player

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.PlaybackException
import androidx.media3.common.Player
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.hls.HlsMediaSource
import androidx.media3.ui.AspectRatioFrameLayout
import com.example.data.model.TvChannel
import com.example.data.repository.ChannelRepository
import com.example.data.stream.StreamResolver
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class PlayerViewModel : ViewModel() {
    private val TAG = "PlayerViewModel"

    private var exoPlayer: ExoPlayer? = null
    private var resolveJob: Job? = null
    private var autoHideControlsJob: Job? = null

    private val _currentChannel = MutableStateFlow<TvChannel?>(null)
    val currentChannel: StateFlow<TvChannel?> = _currentChannel.asStateFlow()

    private val _isPlaying = MutableStateFlow(false)
    val isPlaying: StateFlow<Boolean> = _isPlaying.asStateFlow()

    private val _isBuffering = MutableStateFlow(false)
    val isBuffering: StateFlow<Boolean> = _isBuffering.asStateFlow()

    private val _errorMessage = MutableStateFlow<String?>(null)
    val errorMessage: StateFlow<String?> = _errorMessage.asStateFlow()

    private val _streamQuality = MutableStateFlow("HD Canlı")
    val streamQuality: StateFlow<String> = _streamQuality.asStateFlow()

    private val _resolvedUrl = MutableStateFlow<String?>(null)
    val resolvedUrl: StateFlow<String?> = _resolvedUrl.asStateFlow()

    private val _resizeMode = MutableStateFlow(AspectRatioFrameLayout.RESIZE_MODE_FIT)
    val resizeMode: StateFlow<Int> = _resizeMode.asStateFlow()

    private val _showControls = MutableStateFlow(true)
    val showControls: StateFlow<Boolean> = _showControls.asStateFlow()

    fun getPlayer(context: Context): ExoPlayer {
        if (exoPlayer == null) {
            val player = ExoPlayer.Builder(context.applicationContext).build()
            player.playWhenReady = true
            player.addListener(object : Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    when (playbackState) {
                        Player.STATE_BUFFERING -> {
                            _isBuffering.value = true
                            _errorMessage.value = null
                        }
                        Player.STATE_READY -> {
                            _isBuffering.value = false
                            _isPlaying.value = player.isPlaying
                            _errorMessage.value = null
                        }
                        Player.STATE_ENDED -> {
                            _isBuffering.value = false
                        }
                        Player.STATE_IDLE -> {
                            _isBuffering.value = false
                        }
                    }
                }

                override fun onIsPlayingChanged(playing: Boolean) {
                    _isPlaying.value = playing
                }

                override fun onPlayerError(error: PlaybackException) {
                    Log.e(TAG, "ExoPlayer error: ${error.errorCodeName} - ${error.message}", error)
                    _isBuffering.value = false
                    _isPlaying.value = false
                    _errorMessage.value = "Yayım xətası: ${error.localizedMessage ?: "Bağlantı kəsildi"}"
                }
            })
            exoPlayer = player
        }
        return exoPlayer!!
    }

    fun playChannel(context: Context, channel: TvChannel) {
        _currentChannel.value = channel
        _errorMessage.value = null
        _isBuffering.value = true
        _showControls.value = true
        scheduleAutoHideControls()

        resolveJob?.cancel()
        resolveJob = viewModelScope.launch {
            val resolved = StreamResolver.resolve(channel)
            _resolvedUrl.value = resolved.playUrl
            _streamQuality.value = resolved.resolutionName

            val player = getPlayer(context)

            // Build HttpDataSource with headers
            val dataSourceFactory = DefaultHttpDataSource.Factory()
                .setUserAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
                .setConnectTimeoutMs(10000)
                .setReadTimeoutMs(10000)
                .setAllowCrossProtocolRedirects(true)

            if (resolved.headers.isNotEmpty()) {
                dataSourceFactory.setDefaultRequestProperties(resolved.headers)
            }

            val mediaSource = HlsMediaSource.Factory(dataSourceFactory)
                .setAllowChunklessPreparation(true)
                .createMediaSource(MediaItem.fromUri(Uri.parse(resolved.playUrl)))

            player.stop()
            player.setMediaSource(mediaSource)
            player.prepare()
            player.play()
        }
    }

    fun retry(context: Context) {
        _currentChannel.value?.let { playChannel(context, it) }
    }

    fun nextChannel(context: Context) {
        _currentChannel.value?.let { current ->
            val next = ChannelRepository.getNextChannel(current)
            playChannel(context, next)
        }
    }

    fun prevChannel(context: Context) {
        _currentChannel.value?.let { current ->
            val prev = ChannelRepository.getPrevChannel(current)
            playChannel(context, prev)
        }
    }

    fun togglePlayPause() {
        exoPlayer?.let { player ->
            if (player.isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    fun cycleResizeMode() {
        val nextMode = when (_resizeMode.value) {
            AspectRatioFrameLayout.RESIZE_MODE_FIT -> AspectRatioFrameLayout.RESIZE_MODE_ZOOM
            AspectRatioFrameLayout.RESIZE_MODE_ZOOM -> AspectRatioFrameLayout.RESIZE_MODE_FILL
            else -> AspectRatioFrameLayout.RESIZE_MODE_FIT
        }
        _resizeMode.value = nextMode
    }

    fun toggleControls() {
        _showControls.value = !_showControls.value
        if (_showControls.value) {
            scheduleAutoHideControls()
        } else {
            autoHideControlsJob?.cancel()
        }
    }

    fun showControlsBriefly() {
        _showControls.value = true
        scheduleAutoHideControls()
    }

    private fun scheduleAutoHideControls() {
        autoHideControlsJob?.cancel()
        autoHideControlsJob = viewModelScope.launch {
            delay(4000)
            _showControls.value = false
        }
    }

    fun stopPlayback() {
        resolveJob?.cancel()
        autoHideControlsJob?.cancel()
        exoPlayer?.stop()
        _currentChannel.value = null
        _isPlaying.value = false
        _isBuffering.value = false
        _errorMessage.value = null
    }

    override fun onCleared() {
        super.onCleared()
        resolveJob?.cancel()
        autoHideControlsJob?.cancel()
        exoPlayer?.release()
        exoPlayer = null
    }
}
