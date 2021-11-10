package com.example.seriesmanager.model.episodio

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Episodio(
    val numeroEpisodio: String = "", //primary key
    val nomeEpisodio: String = "",
    val duracao: String = "",
    val assistido: String = "",
    val nomeSerie: String = "",
    val temporadaNumero: String = ""
): Parcelable