package com.hbvhuwe.explorergithub.db

import androidx.room.TypeConverter
import java.net.URL

object UrlConverter {
    @TypeConverter
    @JvmStatic
    fun toUrl(url: String) = URL(url)

    @TypeConverter
    @JvmStatic
    fun fromUrl(url: URL) = url.toString()
}