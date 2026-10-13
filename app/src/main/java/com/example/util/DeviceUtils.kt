package com.example.util

import android.app.Activity
import android.app.UiModeManager
import android.content.Context
import android.content.pm.ActivityInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.view.WindowManager
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

object DeviceUtils {

    fun isTv(context: Context): Boolean {
        val uiModeManager = context.getSystemService(Context.UI_MODE_SERVICE) as? UiModeManager
        if (uiModeManager?.currentModeType == Configuration.UI_MODE_TYPE_TELEVISION) {
            return true
        }
        val pm = context.packageManager
        if (pm.hasSystemFeature(PackageManager.FEATURE_LEANBACK) ||
            pm.hasSystemFeature(PackageManager.FEATURE_TELEVISION) ||
            !pm.hasSystemFeature(PackageManager.FEATURE_TOUCHSCREEN)
        ) {
            return true
        }
        return false
    }

    fun enterPlayerFullscreen(activity: Activity, isTv: Boolean) {
        val window = activity.window
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        insetsController.hide(WindowInsetsCompat.Type.systemBars())

        // On phones: lock to landscape for video player
        if (!isTv) {
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE
        }
    }

    fun exitPlayerFullscreen(activity: Activity, isTv: Boolean) {
        val window = activity.window
        val insetsController = WindowCompat.getInsetsController(window, window.decorView)
        insetsController.show(WindowInsetsCompat.Type.systemBars())

        if (!isTv) {
            // Restore portrait orientation on phones
            activity.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        }
    }
}
