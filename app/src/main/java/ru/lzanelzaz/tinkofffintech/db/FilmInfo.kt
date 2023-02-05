package ru.lzanelzaz.tinkofffintech.db

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import ru.lzanelzaz.tinkofffintech.model.Description

@Entity
data class FilmInfo(
    @PrimaryKey val id: Int,
    @Embedded val description: Description
)
