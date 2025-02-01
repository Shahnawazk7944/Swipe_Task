package dev.data.local.extensions

import android.net.Uri
import androidx.core.net.toUri
import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken


@ProvidedTypeConverter
class UriListTypeConvertor {

    @TypeConverter
    fun fromStringList(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return Gson().fromJson(value, listType) ?: emptyList()
    }

    @TypeConverter
    fun toStringList(list: List<String>?): String {
        return Gson().toJson(list) ?: "[]"
    }

    @TypeConverter
    fun fromUriList(value: String?): List<Uri> {
        return fromStringList(value).map { it.toUri() }
    }

    @TypeConverter
    fun toUriList(list: List<Uri>?): String {
        return toStringList(list?.map { it.toString() })
    }
}