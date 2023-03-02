package com.astro.rocketapp.chathil

import android.app.Application
import android.util.Log
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.request.CachePolicy
import coil.util.DebugLogger
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RocketesApplication : Application(), ImageLoaderFactory {

    override fun newImageLoader(): ImageLoader {
        return ImageLoader.Builder(this)
            .memoryCachePolicy(CachePolicy.ENABLED)
            .diskCachePolicy(CachePolicy.ENABLED)
            .run {
                if (BuildConfig.DEBUG) {
                    logger(DebugLogger(Log.VERBOSE))
                } else {
                    this
                }
            }
            .allowRgb565(true)
            .build()
    }
}
