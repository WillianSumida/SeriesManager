package com.example.seriesmanager.model.temporada

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.seriesmanager.R
import com.example.seriesmanager.model.SerieDao
import com.example.seriesmanager.model.SerieSqlite
import com.example.seriesmanager.model.serie.Serie

class TemporadaSqlite(contexto: Context): TemporadaDao {
    companion object{
        //foreign key
        private val TABELA_SERIE = "serie"
        private val COLUNA_NOME = "nome"

        //table
        private val BD_SERIES_MANAGER = "seriesManager"
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NUMERO_TEMPORADA = "numero_temporada"
        private val COLUNA_ANO_LANCAMENTO = "ano_lancamento"
        private val COLUNA_QTDE_EPISODIOS = "qtde_episodios"
        private val COLUNA_NOME_SERIE = "nome_serie"


        private val CRIAR_TABELA_TEMPORADA_STMT = "CREATE TABLE IF NOT EXISTS ${TABELA_TEMPORADA} (" +
                "${COLUNA_NUMERO_TEMPORADA} INT NOT NULL PRIMARY KEY, " +
                "${COLUNA_ANO_LANCAMENTO} TEXT NOT NULL, " +
                "${COLUNA_QTDE_EPISODIOS} TEXT NOT NULL, " +
                "${COLUNA_NOME_SERIE} TEXT NOT NULL, " +
                "FOREIGN KEY (${COLUNA_NOME_SERIE}) REFERENCES ${TABELA_SERIE}(${COLUNA_NOME}));"
    }

    //Referecia para o db
    private val temporadasDb: SQLiteDatabase
    init{
        temporadasDb = contexto.openOrCreateDatabase(BD_SERIES_MANAGER, Context.MODE_PRIVATE, null)
        try {
            temporadasDb.execSQL(CRIAR_TABELA_TEMPORADA_STMT)
        } catch (se: android.database.SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun createTemporada(temporada: Temporada): Long {
        val temporadaCv = convertTemporadaContentValues(temporada)
        return temporadasDb.insert(TemporadaSqlite.TABELA_TEMPORADA, null, temporadaCv)
    }

    override fun listOneTemporada(numeroDaTemporada: Int): Temporada {
        val temporadaCursor = temporadasDb.query(
            true,
            TABELA_TEMPORADA,
            null, //tabela
            "${COLUNA_NUMERO_TEMPORADA} = ?", //where,
            null, //valores do where
            null,
            null,
            null,
            null
        )

        return if(temporadaCursor.moveToFirst()){
            with(temporadaCursor){
                Temporada(
                    getInt(getColumnIndexOrThrow(COLUNA_NUMERO_TEMPORADA)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getInt(getColumnIndexOrThrow(COLUNA_QTDE_EPISODIOS)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME_SERIE)),
                )
            }
        }
        else{
            Temporada()
        }
    }

    override fun litsAllTemporada(): MutableList<Temporada> {
        val temporadaCursor = temporadasDb.query(
            true,
            TABELA_TEMPORADA,
            null, //tabela
            null, //where,
            arrayOf(), //valores do where
            null,
            null,
            null,
            null
        )

        val temporadasList: MutableList<Temporada> = mutableListOf()
        while(temporadaCursor.moveToNext()){
            with(temporadaCursor){
                temporadasList.add(
                    Temporada(
                        getInt(getColumnIndexOrThrow(COLUNA_NUMERO_TEMPORADA)),
                        getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                        getInt(getColumnIndexOrThrow(COLUNA_QTDE_EPISODIOS)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME_SERIE)),
                    )
                )
            }
        }
        return temporadasList
    }

    override fun updateTemporada(temporada: Temporada): Int {
        val temporadaCv = convertTemporadaContentValues(temporada)
        return temporadasDb.update(TABELA_TEMPORADA, temporadaCv, "${COLUNA_NUMERO_TEMPORADA} = ?", arrayOf(temporada.qtdeEpisodios.toString()))
    }

    override fun deleteTemporada(numeroDaTemporada: Int): Int {
        return temporadasDb.delete(TABELA_TEMPORADA, "${COLUNA_NUMERO_TEMPORADA} = ?",  arrayOf(listOneTemporada(numeroDaTemporada).numeroDaTemporada.toString()))
    }

    private fun convertTemporadaContentValues(temporada: Temporada): ContentValues = ContentValues().also{
        with(it){
            put(TemporadaSqlite.COLUNA_NUMERO_TEMPORADA, temporada.numeroDaTemporada)
            put(TemporadaSqlite.COLUNA_ANO_LANCAMENTO, temporada.anoDeLancamento)
            put(TemporadaSqlite.COLUNA_QTDE_EPISODIOS, temporada.qtdeEpisodios)
            put(TemporadaSqlite.COLUNA_NOME_SERIE, temporada.nomeSerie)
        }
    }
}