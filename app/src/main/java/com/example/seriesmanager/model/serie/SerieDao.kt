package com.example.seriesmanager.model

import com.example.seriesmanager.model.serie.Serie

interface SerieDao {
    fun createSerie(serie: Serie): Long
    fun listOneSerie(nome: String): Serie
    fun litsAllSerie(): MutableList<Serie>
    fun updateSerie(serie: Serie): Int
    fun deleteSerie(nome: String): Int
}