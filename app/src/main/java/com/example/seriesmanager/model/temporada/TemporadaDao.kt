package com.example.seriesmanager.model.temporada

import com.example.seriesmanager.model.serie.Serie

interface TemporadaDao {
    fun createTemporada(temporada: Temporada): Long
    fun listOneTemporada(numeroDaTemporada: Int): Temporada
    fun litsAllTemporada(): MutableList<Temporada>
    fun updateTemporada(temporada: Temporada): Int
    fun deleteTemporada(numeroDaTemporada: Int): Int
}