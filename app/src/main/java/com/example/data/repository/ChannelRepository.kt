package com.example.data.repository

import com.example.data.model.ResolverType
import com.example.data.model.TvChannel

object ChannelRepository {
    val channels: List<TvChannel> = listOf(
        TvChannel(
            id = "trt1",
            channelNumber = 1,
            name = "TRT 1",
            category = "Milli / Ümumi",
            description = "Türkiyənin ilk milli televiziya kanalı. Seriallar, xəbərlər və idman.",
            badgeColorHex = 0xFFE11D48, // Crimson Red
            defaultStreamUrl = "https://trt.daioncdn.net/trt-1/master.m3u8?app=web",
            fallbackStreamUrl = "https://tv-trt1.medya.trt.com.tr/master_720.m3u8",
            resolverType = ResolverType.TRT1,
            customHeaders = mapOf(
                "Origin" to "https://www.tabii.com",
                "Referer" to "https://www.tabii.com/",
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            ),
            isHd = true
        ),
        TvChannel(
            id = "now",
            channelNumber = 2,
            name = "NOW TV",
            category = "Əyləncə / Xəbər",
            description = "Geniş serial, tok-şou və canlı xəbər proqramları.",
            badgeColorHex = 0xFFFF4500, // Vibrant Orange-Red
            defaultStreamUrl = "https://www.nowtv.com.tr/canli-yayin",
            fallbackStreamUrl = "https://nowtv-live-ad.ercdn.net/nowtv/playlist.m3u8",
            resolverType = ResolverType.NOW,
            customHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            ),
            isHd = true
        ),
        TvChannel(
            id = "trt2",
            channelNumber = 3,
            name = "TRT 2",
            category = "Mədəniyyət / İncəsənət",
            description = "Kino, ədəbiyyat, musiqi və sənədli filmlər.",
            badgeColorHex = 0xFF2563EB, // Royal Blue
            defaultStreamUrl = "https://tv-trt2.medya.trt.com.tr/master_1440.m3u8",
            fallbackStreamUrl = "https://tv-trt2.medya.trt.com.tr/master_720.m3u8",
            resolverType = ResolverType.TRT2,
            customHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36"
            ),
            isHd = true
        ),
        TvChannel(
            id = "atv",
            channelNumber = 4,
            name = "ATV TR",
            category = "Serial / Əyləncə",
            description = "Reytinqli seriallar, ailə proqramları və canlı xəbərlər.",
            badgeColorHex = 0xFFF97316, // Rich Amber Orange
            defaultStreamUrl = "https://securevideotoken.tmgrup.com.tr/webtv/secure?938614&url=https%3A%2F%2Ftrkvz-live.ercdn.net%2Fatvhd%2Fatvhd.m3u8&url2=https%3A%2F%2Ftrkvz-live.ercdn.net%2Fatvhd%2Fatvhd.m3u8",
            fallbackStreamUrl = "https://trkvz-live.ercdn.net/atvhd/atvhd.m3u8",
            resolverType = ResolverType.ATV,
            customHeaders = mapOf(
                "Origin" to "https://www.atv.com.tr",
                "Referer" to "https://www.atv.com.tr/",
                "x-isapp" to "false",
                "User-Agent" to "Mozilla/5.0 (Linux; Android 10; K) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Mobile Safari/537.36"
            ),
            isHd = true
        ),
        TvChannel(
            id = "kanald",
            channelNumber = 5,
            name = "KANAL D",
            category = "Əyləncə / Serial",
            description = "Demirören Medya serialları, əyləncə və xəbər buraxılışları.",
            badgeColorHex = 0xFF0284C7, // Sky Blue
            defaultStreamUrl = "https://demiroren.daioncdn.net/kanald/kanald.m3u8?app=kanald_web&ce=3&ppid=f004d745c190e68ecf19e26c7b41ec55&dfp_paln=AQzzBGQEpNcUi3Glpom1PyOUOCMYDjt6In2tqH9c6Bv3rGgaTqExKgdQMByAB6hhHQCxvjzclNCN_IQdDgeCSDMb8d2SQMaZrLsUb1VmQztL3Gi8Zh6QUrBPvCSYUHR1Df3iLUuA6HxeTfbhdcpoQQbhIpxIEfHGYDLZTnvWCFuUWMAGrnQ0qrnk0r2XX1STu-XhuQDYct8-z1v9fW-Cqn3SVAXcfyBr7jxR2ccrtBou0YGsUfrEwUKfmB34xbnbsnHxv18sCa_yQpr_NRtsAQ_PMcN1eOSwaEgDDzyHJ_w00_opO081PJ_82UW4kviHgSPPVqyatVphvnxJutDlClWMbjmmnQygSLXs3WJPGJBiQi19yo1aloNAD2a6aA5PiDlXcO5cJF77jO-vIyVjhpFbKMNMZJK57UgN-Xs_-2-Z_kirAW2pJHrLY7H_t_vF149N074WfZ2_Q-cOz255tuNgdEi3Y3LnsLvZ3O4Z_TUK9Pd-e44PlsO7AfCPBQqFposMI9i41bQ6TMJ2HhXhkrXnz3e8hCTLW2vcJBbekgbkXhU08ZovFSz3f2LC6jmd2wbKl-pdZgA0RdCWB-oJ2uu7Y4HdoreGbhmJPKO7NNdGPFvsj-YA_n2cVkPO1VL1Sc-kTBgb4TKAbdw9i5f6SEh5zovJKkU_lnvECgtUvUURfipUnmm3hTJDypwzvTwjX1F6zhN5qqOUrR5cWigrOSeZtITD-ATacfseeyv59se5kcSCw26TKgNae5MgfZZlFfmRcQrVhR4fPiWczjXgpKGMdZ9oYESYTOAVmtk2Obu84z1piXDKHkeuDQjFgZfG-M77KUCJR4ZR8cwdjlC1pt7ZJg7EzZSZ8UA9KtS2IbXg4bQTKqGZyhgipddKJVFf0mKIwI97QiuxjM5H3TQxuBc2Av0B2J02MMt9XQdXymS12K1-b7pX6a5mXzu6U8jIvjfld8UazkFhkYH2jm_xWVKeOXqtM6BBm1cL_id7gFu3ja7V2oblPKW1MwgXQSBrhQyo1ngIEDaUihVW4Cnu6gd0BHmLttbn8L3CPgI53Jh0ORbo0wh2rWAacIt_TBDN7bjPNEeeaaol3eqXTBAe5zsJmjXWGr8Yl0f0BgPi2YwqqiMHzelO-O44KVXnIax-Cq-9phjbHffcPuAetNexU49qY3DM07oPoJNa1-oII7j2rvWTyEc3x1aZ6EYAQgcNgWSjJhTEVfchXUedz--zmiytFLdSrTn0NoXcCVXCutWggtUfKYeLFVg9OYbqVZjQ-ZBKLq2M4o-KUinWtuwKBFPBShYujylEKouxvBEp0ZhqBwegrx1mW88hFaomNuJO_j-fQq4vhjFjZdjRqOnyaOJtY-97gB-fCd31t_5OlekWnDgv5dQ_QoL13Z7lMs0IJNJfavxyDXllXDs4YHSvaJpeImy6ApVY9b03KcOryhmJv_8rYFGpOwcEZtmX8OuUkCyduAsu0b9BNYGLBojHC4tFov2AaI5QibfCXd9siay6x_FBjXwLBNs05MyeRgtqrXqb4wGWaR02Icb1CPBKxbxE5F2XKcDt_ldee0Z7QhpHaF8x8_R4fLtSb8ldOhn045HUNqs.",
            fallbackStreamUrl = "https://kdlive.duhnet.tv/S2/HLS_LIVE/kanalddainp/playlist.m3u8",
            resolverType = ResolverType.KANALD,
            customHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            ),
            isHd = true
        ),
        TvChannel(
            id = "showtv",
            channelNumber = 6,
            name = "Show TV",
            category = "Əyləncə / Şou",
            description = "Türkiyənin ən sevilən komediya, drama və şou verilişləri.",
            badgeColorHex = 0xFF8B5CF6, // Purple
            defaultStreamUrl = "https://www.showtv.com.tr/canli-yayin",
            fallbackStreamUrl = "https://ciner-live.ercdn.net/showturk/playlist.m3u8",
            resolverType = ResolverType.SHOWTV,
            customHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            ),
            isHd = true
        ),
        TvChannel(
            id = "startv",
            channelNumber = 7,
            name = "Star TV",
            category = "Serial / Kino",
            description = "Geniş auditoriyalı dram serialları və axşam xəbərləri.",
            badgeColorHex = 0xFFDC2626, // Red
            defaultStreamUrl = "https://dogus.daioncdn.net/startv/startv.m3u8?ce=3&app=a20ac41e-bdc3-4aa1-934d-26b484480ac9",
            fallbackStreamUrl = "https://www.startv.com.tr/canli-yayin",
            resolverType = ResolverType.STARTV,
            customHeaders = mapOf(
                "User-Agent" to "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36"
            ),
            isHd = true
        )
    )

    fun getChannelById(id: String): TvChannel? = channels.find { it.id == id }

    fun getChannelByNumber(number: Int): TvChannel? = channels.find { it.channelNumber == number }

    fun getNextChannel(current: TvChannel): TvChannel {
        val index = channels.indexOfFirst { it.id == current.id }
        return if (index >= 0 && index < channels.size - 1) channels[index + 1] else channels.first()
    }

    fun getPrevChannel(current: TvChannel): TvChannel {
        val index = channels.indexOfFirst { it.id == current.id }
        return if (index > 0) channels[index - 1] else channels.last()
    }
}
