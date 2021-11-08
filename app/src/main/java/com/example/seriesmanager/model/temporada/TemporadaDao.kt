package com.example.seriesmanager.model.temporada

import com.example.seriesmanager.model.serie.Serie

interface TemporadaDao {
    fun createTemporada(temporada: Temporada): Long
    fun listOneTemporada(temporada: Temporada): Temporada
    fun litsAllTemporada(nomeSerie: String): MutableList<Temporada>
    fun updateTemporada(temporada: Temporada): Int
    fun deleteTemporada(numeroTemporada: String, nomeSerie: String): Int
}