package com.example.seriesmanager.controller.serie

import com.example.seriesmanager.MainActivity
import com.example.seriesmanager.model.SerieDao
import com.example.seriesmanager.model.SerieSqlite
import com.example.seriesmanager.model.firebase.SerieFirebase
import com.example.seriesmanager.model.serie.Serie

class SerieController(mainActivity: MainActivity) {
    private val serieDao: SerieDao = SerieFirebase()

    fun insertSerie(serie: Serie) = serieDao.createSerie(serie);
    fun listOneSerie(nome: String) = serieDao.listOneSerie(nome);
    fun listAllSerie() = serieDao.litsAllSerie();
    fun updateSerie(serie: Serie) = serieDao.updateSerie(serie);
    fun deleteSerie(nome: String) = serieDao.deleteSerie(nome);
}