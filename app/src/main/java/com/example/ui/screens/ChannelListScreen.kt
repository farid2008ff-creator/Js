package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tv
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.TvChannel
import com.example.data.repository.ChannelRepository
import com.example.ui.components.ChannelCard
import com.example.ui.components.LiveIndicatorBadge
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.CyanGlow
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.RedDark
import com.example.ui.theme.RedPrimary
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChannelListScreen(
    isTv: Boolean,
    onChannelSelected: (TvChannel) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategory by remember { mutableStateOf("Hamısı") }

    val categories = listOf("Hamısı", "Milli", "Serial", "Mədəniyyət", "Əyləncə")

    val filteredChannels = remember(searchQuery, selectedCategory) {
        ChannelRepository.channels.filter { channel ->
            val matchesSearch = channel.name.contains(searchQuery, ignoreCase = true) ||
                    channel.category.contains(searchQuery, ignoreCase = true) ||
                    channel.channelNumber.toString() == searchQuery.trim()

            val matchesCategory = when (selectedCategory) {
                "Hamısı" -> true
                "Milli" -> channel.category.contains("Milli", ignoreCase = true)
                "Serial" -> channel.category.contains("Serial", ignoreCase = true)
                "Mədəniyyət" -> channel.category.contains("Mədəniyyət", ignoreCase = true)
                "Əyləncə" -> channel.category.contains("Əyləncə", ignoreCase = true)
                else -> true
            }

            matchesSearch && matchesCategory
        }
    }

    Scaffold(
        modifier = modifier.testTag("channel_list_screen"),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(38.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    Brush.linearGradient(listOf(RedPrimary, RedDark))
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LiveTv,
                                contentDescription = "Canlı TV",
                                tint = Color.White,
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "Canlı TV",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 19.sp,
                                    color = TextPrimary
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                LiveIndicatorBadge(text = "CANLI")
                            }
                            Text(
                                text = "Türkiyənin 7 Əsas Kanalı",
                                fontSize = 11.sp,
                                color = TextMuted
                            )
                        }
                    }
                },
                actions = {
                    Box(
                        modifier = Modifier
                            .padding(end = 12.dp)
                            .background(
                                color = if (isTv) CyanAccent.copy(alpha = 0.2f) else Color.White.copy(alpha = 0.08f),
                                shape = RoundedCornerShape(14.dp)
                            )
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = if (isTv) "📺 Android TV" else "📱 Mobil / Yatay Avto",
                            color = if (isTv) CyanGlow else TextSecondary,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = DarkBackground
                )
            )
        }
    ) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val gridColumns = when {
                maxWidth > 900.dp -> 4
                maxWidth > 600.dp -> 3
                maxWidth > 400.dp -> 2
                else -> 1
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(gridColumns),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                // Header Search & Category Filter
                item(span = { GridItemSpan(gridColumns) }) {
                    Column {
                        // Search bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("channel_search_input"),
                            placeholder = { Text("Kanal axtarışı (məs. TRT, ATV, NOW, 1-7)...", color = TextMuted) },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Axtarış",
                                    tint = TextSecondary
                                )
                            },
                            singleLine = true,
                            shape = RoundedCornerShape(14.dp),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedContainerColor = DarkSurface,
                                unfocusedContainerColor = DarkSurface,
                                focusedBorderColor = CyanGlow,
                                unfocusedBorderColor = DarkBorder,
                                focusedTextColor = TextPrimary,
                                unfocusedTextColor = TextPrimary
                            )
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Category chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            categories.forEach { cat ->
                                val selected = selectedCategory == cat
                                FilterChip(
                                    selected = selected,
                                    onClick = { selectedCategory = cat },
                                    label = { Text(cat, fontSize = 12.sp, fontWeight = FontWeight.Medium) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = RedPrimary,
                                        selectedLabelColor = Color.White,
                                        containerColor = DarkSurface,
                                        labelColor = TextSecondary
                                    ),
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = selected,
                                        borderColor = if (selected) RedPrimary else DarkBorder
                                    )
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        // Quick info banner
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    color = DarkSurface,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Tv,
                                    contentDescription = null,
                                    tint = CyanGlow,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (isTv) {
                                        "İstədiyiniz kanalı seçin və ya pultdakı 1-7 rəqəmlərindən istifadə edin. Avto tam ekran."
                                    } else {
                                        "Kanalı seçdikdə avtomatik yatay (landscape) və tam ekran rejiminə keçəcək."
                                    },
                                    color = TextSecondary,
                                    fontSize = 12.sp,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                // Channel Cards
                items(filteredChannels, key = { it.id }) { channel ->
                    ChannelCard(
                        channel = channel,
                        onClick = { onChannelSelected(channel) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
