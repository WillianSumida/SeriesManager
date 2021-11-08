package com.example.seriesmanager.model.episodio

import com.example.seriesmanager.model.serie.Serie

interface EpisodioDao {
    fun createEpisodio(episodio: Episodio): Long
    fun listOneEpisodio(episodio: Episodio): Episodio
    fun listAllEpisodio(nomeSerie: String, temporada: String): MutableList<Episodio>
    fun updateEpisodio(episodio: Episodio): Int
    fun deleteEpisodio(episodio: Episodio): Int
}