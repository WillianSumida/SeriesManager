package com.example.seriesmanager.controller.episodio

import com.example.seriesmanager.MainEpisodioActivity
import com.example.seriesmanager.model.episodio.Episodio
import com.example.seriesmanager.model.episodio.EpisodioDao
import com.example.seriesmanager.model.episodio.EpisodioSqlite
import com.example.seriesmanager.model.firebase.EpisodioFirebase
import com.example.seriesmanager.model.temporada.Temporada

class EpisodioController (mainEpisodioActivity: MainEpisodioActivity){
    private val episodioDao: EpisodioDao = EpisodioFirebase()

    fun insertEpisodio(episodio: Episodio) = episodioDao.createEpisodio(episodio)
    fun listOneEpisodio(episodio: Episodio) = episodioDao.listOneEpisodio(episodio)
    fun listAllEpisodio(nomeSerie: String, temporada: String) = episodioDao.listAllEpisodio(nomeSerie, temporada)
    fun updateEpisodio(episodio: Episodio) = episodioDao.updateEpisodio(episodio)
    fun deleteEpisodio(episodio: Episodio) = episodioDao.deleteEpisodio(episodio)
}