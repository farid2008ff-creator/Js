package com.example.data.model

enum class ResolverType {
    DIRECT,
    TRT1,
    TRT2,
    NOW,
    ATV,
    KANALD,
    SHOWTV,
    STARTV
}

data class TvChannel(
    val id: String,
    val channelNumber: Int,
    val name: String,
    val category: String,
    val description: String,
    val badgeColorHex: Long,
    val defaultStreamUrl: String,
    val fallbackStreamUrl: String? = null,
    val resolverType: ResolverType = ResolverType.DIRECT,
    val customHeaders: Map<String, String> = emptyMap(),
    val isHd: Boolean = true
)
