package com.example.data.stream

import android.util.Log
import com.example.data.model.ResolverType
import com.example.data.model.TvChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

data class ResolvedStream(
    val playUrl: String,
    val headers: Map<String, String>,
    val resolutionName: String = "HD Canlı"
)

object StreamResolver {
    private const val TAG = "StreamResolver"

    private val client = OkHttpClient.Builder()
        .connectTimeout(8, TimeUnit.SECONDS)
        .readTimeout(8, TimeUnit.SECONDS)
        .followRedirects(true)
        .build()

    suspend fun resolve(channel: TvChannel): ResolvedStream = withContext(Dispatchers.IO) {
        try {
            when (channel.resolverType) {
                ResolverType.TRT1 -> resolveTrt1(channel)
                ResolverType.NOW -> resolveNow(channel)
                ResolverType.ATV -> resolveAtv(channel)
                ResolverType.KANALD -> resolveKanalD(channel)
                ResolverType.SHOWTV -> resolveShowTv(channel)
                ResolverType.STARTV -> resolveStarTv(channel)
                ResolverType.TRT2, ResolverType.DIRECT -> ResolvedStream(
                    playUrl = channel.defaultStreamUrl,
                    headers = channel.customHeaders,
                    resolutionName = "1080p FHD"
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error resolving channel ${channel.name}: ${e.message}", e)
            // Fallback to default or fallbackStreamUrl
            val fallback = channel.fallbackStreamUrl ?: channel.defaultStreamUrl
            ResolvedStream(
                playUrl = fallback,
                headers = channel.customHeaders,
                resolutionName = "Ehtiyat Yayım"
            )
        }
    }

    private fun resolveTrt1(channel: TvChannel): ResolvedStream {
        val request = Request.Builder()
            .url(channel.defaultStreamUrl)
            .addHeader("Origin", "https://www.tabii.com")
            .addHeader("Referer", "https://www.tabii.com/")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36")
            .addHeader("Accept", "*/*")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body?.string().orEmpty()
                val lines = body.lines()
                // Find 1080p or first .m3u8 line
                var candidate: String? = null
                for (line in lines) {
                    val trimmed = line.trim()
                    if (!trimmed.startsWith("#") && trimmed.contains(".m3u8")) {
                        if (trimmed.contains("1080p")) {
                            candidate = trimmed
                            break
                        }
                        if (candidate == null) {
                            candidate = trimmed
                        }
                    }
                }

                if (!candidate.isNullOrBlank()) {
                    val finalUrl = if (candidate.startsWith("http")) {
                        candidate
                    } else {
                        "https://trt.daioncdn.net/trt-1/$candidate"
                    }
                    return ResolvedStream(
                        playUrl = finalUrl,
                        headers = channel.customHeaders,
                        resolutionName = "FHD Canlı"
                    )
                }
            }
        }

        return ResolvedStream(
            playUrl = channel.defaultStreamUrl,
            headers = channel.customHeaders,
            resolutionName = "HD Canlı"
        )
    }

    private fun resolveNow(channel: TvChannel): ResolvedStream {
        val request = Request.Builder()
            .url("https://www.nowtv.com.tr/canli-yayin")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body?.string().orEmpty()
                val m3u8Regex = Pattern.compile("https?://[^\"'\\s<>]+\\.m3u8[^\"'\\s<>]*")
                val matcher = m3u8Regex.matcher(body)
                while (matcher.find()) {
                    var found = matcher.group()
                    // clean potential trailing characters like ' or )
                    found = found.trimEnd('\'', ')', ',', '"')
                    if (found.contains("nowtv.daioncdn.net") || found.contains("nowtv")) {
                        return ResolvedStream(
                            playUrl = found,
                            headers = channel.customHeaders,
                            resolutionName = "HD Canlı"
                        )
                    }
                }
            }
        }

        return ResolvedStream(
            playUrl = channel.fallbackStreamUrl ?: "https://nowtv-live-ad.ercdn.net/nowtv/playlist.m3u8",
            headers = channel.customHeaders,
            resolutionName = "HD Canlı"
        )
    }

    private fun resolveAtv(channel: TvChannel): ResolvedStream {
        val rand = System.currentTimeMillis()
        val request = Request.Builder()
            .url(channel.defaultStreamUrl)
            .addHeader("Origin", "https://www.atv.com.tr")
            .addHeader("Referer", "https://www.atv.com.tr/")
            .addHeader("x-rand", rand.toString())
            .addHeader("x-isapp", "false")
            .addHeader("User-Agent", "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36")
            .addHeader("Accept", "*/*")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val jsonStr = response.body?.string().orEmpty()
                if (jsonStr.isNotBlank()) {
                    val jsonObj = JSONObject(jsonStr)
                    val url = jsonObj.optString("Url").ifBlank { jsonObj.optString("AlternateUrl") }
                    if (url.isNotBlank() && url.startsWith("http")) {
                        return ResolvedStream(
                            playUrl = url,
                            headers = mapOf(
                                "Origin" to "https://www.atv.com.tr",
                                "Referer" to "https://www.atv.com.tr/",
                                "User-Agent" to "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
                            ),
                            resolutionName = "HD Tokenli"
                        )
                    }
                }
            }
        }

        return ResolvedStream(
            playUrl = channel.fallbackStreamUrl ?: "https://trkvz-live.ercdn.net/atvhd/atvhd.m3u8",
            headers = channel.customHeaders,
            resolutionName = "HD Canlı"
        )
    }

    private fun resolveKanalD(channel: TvChannel): ResolvedStream {
        // Try scraping from page first for the freshest token
        try {
            val pageRequest = Request.Builder()
                .url("https://www.kanald.com.tr/canli-yayin")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
                .build()
            client.newCall(pageRequest).execute().use { response ->
                if (response.isSuccessful) {
                    val body = response.body?.string().orEmpty()
                    val m3u8Regex = Pattern.compile("https?://[^\"'\\s<>]+\\.m3u8[^\"'\\s<>]*")
                    val matcher = m3u8Regex.matcher(body)
                    while (matcher.find()) {
                        val found = matcher.group().replace("&amp;", "&").trimEnd('\'', ')', ',', '"')
                        if (found.contains("kanald") && !found.contains("duhnet")) {
                            return ResolvedStream(
                                playUrl = found,
                                headers = channel.customHeaders,
                                resolutionName = "HD Canlı"
                            )
                        }
                    }
                }
            }
        } catch (e: Exception) {
            Log.w(TAG, "Kanal D page scrape failed: ${e.message}")
        }

        return ResolvedStream(
            playUrl = channel.defaultStreamUrl,
            headers = channel.customHeaders,
            resolutionName = "HD Canlı"
        )
    }

    private fun resolveShowTv(channel: TvChannel): ResolvedStream {
        val request = Request.Builder()
            .url("https://www.showtv.com.tr/canli-yayin")
            .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36")
            .build()

        client.newCall(request).execute().use { response ->
            if (response.isSuccessful) {
                val body = response.body?.string().orEmpty()
                val m3u8Regex = Pattern.compile("https?://[^\"'\\s<>]+\\.m3u8[^\"'\\s<>]*")
                val matcher = m3u8Regex.matcher(body)
                while (matcher.find()) {
                    var found = matcher.group().trimEnd('\'', ')', ',', '"')
                    if (found.contains("showtv.daioncdn.net") || (found.contains("showtv") && found.contains("daioncdn"))) {
                        return ResolvedStream(
                            playUrl = found,
                            headers = channel.customHeaders,
                            resolutionName = "HD Canlı"
                        )
                    }
                }
            }
        }

        return ResolvedStream(
            playUrl = channel.fallbackStreamUrl ?: "https://ciner-live.ercdn.net/showturk/playlist.m3u8",
            headers = channel.customHeaders,
            resolutionName = "HD Canlı"
        )
    }

    private fun resolveStarTv(channel: TvChannel): ResolvedStream {
        return ResolvedStream(
            playUrl = channel.defaultStreamUrl,
            headers = channel.customHeaders,
            resolutionName = "HD Canlı"
        )
    }
}
