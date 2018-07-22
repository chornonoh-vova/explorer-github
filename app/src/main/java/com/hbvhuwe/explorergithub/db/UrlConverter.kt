package com.hbvhuwe.explorergithub.db

import android.arch.persistence.room.TypeConverter
import java.net.URL

object UrlConverter {
    @TypeConverter
    @JvmStatic
    fun toUrl(url: String) = URL(url)

    @TypeConverter
    @JvmStatic
    fun fromUrl(url: URL) = url.toString()
}