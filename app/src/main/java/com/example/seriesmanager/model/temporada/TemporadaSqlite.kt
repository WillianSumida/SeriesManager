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
        private val BD_SERIES_MANAGER = "seriesManager"
        private val TABELA_TEMPORADA = "temporada"
        private val COLUNA_NUMERO_TEMPORADA = "numero_temporada"
        private val COLUNA_ANO_LANCAMENTO = "ano_lancamento"
        private val COLUNA_QTD_EPISODES = "qtd_episodes"
        private val COLUNA_NOME_SERIE = "nome_Serie"


        private val CRIAR_TABELA_TEMPORADA_STMT = "CREATE TABLE IF NOT EXISTS $TABELA_TEMPORADA (" +
                "$COLUNA_NUMERO_TEMPORADA TEXT NOT NULL, " +
                "$COLUNA_ANO_LANCAMENTO TEXT NOT NULL, " +
                "$COLUNA_QTD_EPISODES TEXT NOT NULL, " +
                "$COLUNA_NOME_SERIE TEXT NOT NULL);"
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
        return temporadasDb.insert(TABELA_TEMPORADA, null, temporadaCv)
    }

    override fun listOneTemporada(numeroTemporada: String): Temporada {
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
                    getString(getColumnIndexOrThrow(COLUNA_NUMERO_TEMPORADA)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getString(getColumnIndexOrThrow(COLUNA_QTD_EPISODES)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME_SERIE)),
                )
            }
        }
        else{
            Temporada()
        }
    }

    override fun litsAllTemporada(nomeSerie: String): MutableList<Temporada> {
        val temporadasCursor = temporadasDb.query(
            true,
            TABELA_TEMPORADA,
            null, //tabela
            "${COLUNA_NOME_SERIE} = ?", //where,

            arrayOf(nomeSerie), //valores do where
            null,
            null,
            null,
            null
        )

        val temporadasList: MutableList<Temporada> = mutableListOf()
        while(temporadasCursor.moveToNext()){
            with(temporadasCursor){
                temporadasList.add(
                    Temporada(
                        getString(getColumnIndexOrThrow(COLUNA_NUMERO_TEMPORADA)),
                        getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                        getString(getColumnIndexOrThrow(COLUNA_QTD_EPISODES)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME_SERIE)),
                    )
                )
            }
        }
        return temporadasList
    }

    override fun updateTemporada(temporada: Temporada): Int {
        val temporadaCv = convertTemporadaContentValues(temporada)
        return temporadasDb.update(TABELA_TEMPORADA, temporadaCv, "${COLUNA_NUMERO_TEMPORADA} = ?", arrayOf(temporada.numeroTemporada))
    }

    override fun deleteTemporada(numeroTemporada: String): Int {
        return temporadasDb.delete(TABELA_TEMPORADA, "${COLUNA_NUMERO_TEMPORADA} = ?", arrayOf(numeroTemporada))
    }

    private fun convertTemporadaContentValues(temporada: Temporada): ContentValues = ContentValues().also{
        with(it){
            put(COLUNA_NUMERO_TEMPORADA, temporada.numeroTemporada)
            put(COLUNA_ANO_LANCAMENTO, temporada.anoDeLancamento)
            put(COLUNA_QTD_EPISODES, temporada.qtdEpisodes)
            put(COLUNA_NOME_SERIE, temporada.nomeSerie)
        }
    }
}