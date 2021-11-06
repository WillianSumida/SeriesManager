package com.example.seriesmanager.model.temporada

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Temporada(
    val numeroTemporada: String = "", //primary key
    val anoDeLancamento: String = "",
    val qtdEpisodes: String = "",
    val nomeSerie: String = "",
): Parcelable {
}