package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.repository.ChannelRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

    @Test
    fun `read app_name string from context`() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        val appName = context.getString(R.string.app_name)
        assertEquals("Canlı TV", appName)
    }

    @Test
    fun `verify all 7 channels are present in repository`() {
        val channels = ChannelRepository.channels
        assertEquals(7, channels.size)

        val channelIds = channels.map { it.id }
        assertTrue(channelIds.contains("trt1"))
        assertTrue(channelIds.contains("now"))
        assertTrue(channelIds.contains("trt2"))
        assertTrue(channelIds.contains("atv"))
        assertTrue(channelIds.contains("kanald"))
        assertTrue(channelIds.contains("showtv"))
        assertTrue(channelIds.contains("startv"))

        val trt1 = ChannelRepository.getChannelByNumber(1)
        assertNotNull(trt1)
        assertEquals("TRT 1", trt1?.name)
    }

    @Test
    fun `verify next and previous channel cycle`() {
        val first = ChannelRepository.channels.first()
        val next = ChannelRepository.getNextChannel(first)
        assertEquals(2, next.channelNumber)

        val prev = ChannelRepository.getPrevChannel(first)
        assertEquals(7, prev.channelNumber)
    }
}
