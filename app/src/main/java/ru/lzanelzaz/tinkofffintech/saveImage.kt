package ru.lzanelzaz.tinkofffintech

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import androidx.core.net.toUri
import coil.ImageLoader
import coil.request.ImageRequest
import java.io.File

fun saveImage(fileName: String, posterUrl: String, context: Context) {
    val req = ImageRequest.Builder(context)
        .data(
            posterUrl.toUri().buildUpon().scheme("https").build()
        )
        .target { result ->
            val outputStream = File(context.filesDir, fileName).outputStream()
            (result as BitmapDrawable).bitmap
                .compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
        }.build()
    ImageLoader(context).enqueue(req)
}