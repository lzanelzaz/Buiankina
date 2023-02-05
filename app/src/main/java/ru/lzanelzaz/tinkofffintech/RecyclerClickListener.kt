package ru.lzanelzaz.tinkofffintech

import android.graphics.drawable.Drawable

interface RecyclerClickListener {
    fun onItemRemoveClick(filmId: Int)
    fun onItemClick(filmId: Int, drawable: Drawable)
}