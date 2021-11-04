package com.example.seriesmanager.model.temporada

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Temporada(
    val numeroDaTemporada: Int = 0, //primary key
    val anoDeLancamento: String = "",
    val qtdeEpisodios: Int = 0,
    val nomeSerie: String = "",
): Parcelable