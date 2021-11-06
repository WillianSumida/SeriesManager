package com.example.seriesmanager.controller.temporada

import com.example.seriesmanager.MainActivity
import com.example.seriesmanager.model.temporada.Temporada
import com.example.seriesmanager.model.temporada.TemporadaDao
import com.example.seriesmanager.model.temporada.TemporadaSqlite

//VERIFCAR SE O PARAM MAINACTIVITY
class TemporadaController (mainActivity: MainActivity){
    private val temporadaDao: TemporadaDao = TemporadaSqlite(mainActivity)

    fun insertTemporada(temporada: Temporada) = temporadaDao.createTemporada(temporada);
    fun listOneTemporada(numeroTemporada: String) = temporadaDao.listOneTemporada(numeroTemporada);
    fun listAllTemporada(nomeSerie: String) = temporadaDao.litsAllTemporada(nomeSerie);
    fun updateTemporada(temporada: Temporada) = temporadaDao.updateTemporada(temporada);
    fun deleteTemporada(numeroTemporada: String) = temporadaDao.deleteTemporada(numeroTemporada);
}