package com.example.seriesmanager.controller.temporada

import com.example.seriesmanager.MainActivity
import com.example.seriesmanager.MainTemporadaActivity
import com.example.seriesmanager.model.temporada.Temporada
import com.example.seriesmanager.model.temporada.TemporadaDao
import com.example.seriesmanager.model.temporada.TemporadaSqlite

//VERIFCAR SE O PARAM MAINACTIVITY
class TemporadaController (mainTemporadaActivity: MainTemporadaActivity){
    private val temporadaDao: TemporadaDao = TemporadaSqlite(mainTemporadaActivity)

    fun insertTemporada(temporada: Temporada) = temporadaDao.createTemporada(temporada);
    fun listOneTemporada(temporada: Temporada) = temporadaDao.listOneTemporada(temporada);
    fun listAllTemporada(nomeSerie: String) = temporadaDao.litsAllTemporada(nomeSerie);
    fun updateTemporada(temporada: Temporada) = temporadaDao.updateTemporada(temporada);
    fun deleteTemporada(numeroTemporada: String) = temporadaDao.deleteTemporada(numeroTemporada);
}