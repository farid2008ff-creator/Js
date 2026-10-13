package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TvChannel
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun ChannelCard(
    channel: TvChannel,
    isCurrentPlaying: Boolean = false,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isFocused by remember { mutableStateOf(false) }

    val scale by animateFloatAsState(
        targetValue = if (isFocused) 1.04f else 1f,
        animationSpec = spring(dampingRatio = 0.75f, stiffness = 300f),
        label = "cardScale"
    )

    val borderColor by animateColorAsState(
        targetValue = when {
            isFocused -> CyanGlow
            isCurrentPlaying -> Color(0xFFEF4444)
            else -> DarkBorder
        },
        label = "borderColor"
    )

    val borderWidth = if (isFocused || isCurrentPlaying) 2.dp else 1.dp
    val badgeColor = Color(channel.badgeColorHex)

    Card(
        modifier = modifier
            .testTag("channel_card_${channel.id}")
            .scale(scale)
            .clip(RoundedCornerShape(16.dp))
            .border(borderWidth, borderColor, RoundedCornerShape(16.dp))
            .onFocusChanged { isFocused = it.isFocused }
            .focusable()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isFocused) DarkSurfaceVariant else DarkSurface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (isFocused) 8.dp else 2.dp
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            // Header: Channel Number & Badges
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Number
                Box(
                    modifier = Modifier
                        .background(
                            color = if (isFocused) CyanGlow.copy(alpha = 0.25f) else Color.White.copy(alpha = 0.08f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = String.format("%02d", channel.channelNumber),
                        color = if (isFocused) CyanGlow else TextSecondary,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (channel.isHd) {
                        Box(
                            modifier = Modifier
                                .background(
                                    color = Color(0xFF0284C7).copy(alpha = 0.2f),
                                    shape = RoundedCornerShape(6.dp)
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "HD",
                                color = Color(0xFF38BDF8),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                    }
                    LiveIndicatorBadge()
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Channel Logo Emblem & Channel Name
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Channel Logo Monogram
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(badgeColor, badgeColor.copy(alpha = 0.7f))
                            )
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Tv,
                        contentDescription = channel.name,
                        tint = Color.White,
                        modifier = Modifier.size(24.dp)
                    )
                }

                Spacer(modifier = Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = channel.name,
                        color = TextPrimary,
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = channel.category,
                        color = badgeColor.copy(alpha = 0.9f),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                // Play icon button affordance
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(
                            if (isFocused) CyanGlow else Color.White.copy(alpha = 0.1f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Oynat",
                        tint = if (isFocused) Color.Black else Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Description
            Text(
                text = channel.description,
                color = TextMuted,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}
