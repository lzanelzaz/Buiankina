package ru.lzanelzaz.tinkofffintech.db

import android.graphics.drawable.Drawable
import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import ru.lzanelzaz.tinkofffintech.model.Country
import ru.lzanelzaz.tinkofffintech.model.Description
import ru.lzanelzaz.tinkofffintech.model.Genre

private fun <T> T.toJson(): String = Gson().toJson(this)

inline fun <reified T> String.fromJson(): T = Gson().fromJson(this, T::class.java)

class Converters {
    @TypeConverter
    fun filmToJson(it: Description) = it.toJson()

    @TypeConverter
    fun filmFromJson(src: String): Description = src.fromJson()

    @TypeConverter
    fun drawableToJson(it: Drawable) = it.toJson()

    @TypeConverter
    fun drawableFromJson(src: String): Drawable = src.fromJson()

    @TypeConverter
    fun genreToJson(it: List<Genre>) = it.toJson()

    @TypeConverter
    fun genreFromJson(src: String): List<Genre> =
        Gson().fromJson(src, object : TypeToken<List<Genre>>() {}.type)

    @TypeConverter
    fun countryToJson(it: List<Country>) = it.toJson()

    @TypeConverter
    fun countryFromJson(src: String): List<Country> =
        Gson().fromJson(src, object : TypeToken<List<Country>>() {}.type)
}