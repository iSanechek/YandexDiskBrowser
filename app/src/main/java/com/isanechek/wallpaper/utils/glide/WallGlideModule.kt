package com.isanechek.wallpaper.utils.glide

import android.app.ActivityManager
import android.content.Context
import android.support.v4.app.ActivityManagerCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.integration.okhttp3.OkHttpUrlLoader
import com.bumptech.glide.load.DecodeFormat
import com.bumptech.glide.load.engine.cache.LruResourceCache
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import okhttp3.OkHttpClient
import java.io.InputStream

/**
 * Created by isanechek on 11/6/17.
 */
@GlideModule
class WallGlideModule : AppGlideModule() {

    override fun applyOptions(context: Context?, builder: GlideBuilder) {
        val activityManager = context?.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val memoryCacheSizeBytes = 1024 * 1024 * 20 // 20mb
        builder.setMemoryCache(LruResourceCache(memoryCacheSizeBytes.toLong()))
        builder.setDefaultRequestOptions(RequestOptions().format(
                if (ActivityManagerCompat.isLowRamDevice(activityManager)) {
                    DecodeFormat.PREFER_RGB_565
                } else {
                    DecodeFormat.PREFER_ARGB_8888
                })
        )
    }

    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        super.registerComponents(context, glide, registry)
        val client = OkHttpClient.Builder().build()
        val factor = OkHttpUrlLoader.Factory(client)
        glide.registry.replace(GlideUrl::class.java, InputStream::class.java, factor)
    }
}