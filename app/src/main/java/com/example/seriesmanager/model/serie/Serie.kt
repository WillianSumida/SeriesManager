package com.example.seriesmanager.model.serie

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Serie(
    val nome: String = "", //primary key
    val anoDeLancamento: String = "",
    val emissora: String = "",
    val genero: String = "",
): Parcelable