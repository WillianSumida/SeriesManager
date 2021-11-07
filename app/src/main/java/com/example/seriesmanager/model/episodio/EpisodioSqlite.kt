package com.example.seriesmanager.model.episodio

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.seriesmanager.R
import com.example.seriesmanager.model.temporada.Temporada
import com.example.seriesmanager.model.temporada.TemporadaSqlite

class EpisodioSqlite(contexto: Context): EpisodioDao {
    companion object {
        private val BD_SERIES_MANAGER = "seriesManager"
        private val TABELA_EPISODIO = "episodio"
        private val COLUNA_NUMERO_EPISODIO = "numeroEpisodio"
        private val COLUNA_NOME_EPISODIO = "nomeEpisodio"
        private val COLUNA_DURACAO = "duracao"
        private val COLUNA_ASSISTIDO = "assitido"
        private val COLUNA_NOME_SERIE = "nomeSerie"
        private val COLUNA_TEMPORADA = "temporada"


        private val CRIAR_TABELA_EPISODIO_STMT = "CREATE TABLE IF NOT EXISTS $TABELA_EPISODIO(" +
                "$COLUNA_NUMERO_EPISODIO TEXT NOT NULL, " +
                "$COLUNA_NOME_EPISODIO TEXT NOT NULL, " +
                "$COLUNA_DURACAO TEXT NOT NULL, " +
                "$COLUNA_ASSISTIDO TEXT NOT NULL, " +
                "$COLUNA_NOME_SERIE TEXT NOT NULL, " +
                "$COLUNA_TEMPORADA TEXT NOT NULL);"
    }

    //Referecia para o db
    private val episodiosDb: SQLiteDatabase
    init{
        episodiosDb = contexto.openOrCreateDatabase(EpisodioSqlite.BD_SERIES_MANAGER, Context.MODE_PRIVATE, null)
        try {
            episodiosDb.execSQL(EpisodioSqlite.CRIAR_TABELA_EPISODIO_STMT)
        } catch (se: android.database.SQLException){
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun createEpisodio(episodio: Episodio): Long {
        val episodioCv = convertEpisodioContentValues(episodio)
        return episodiosDb.insert(TABELA_EPISODIO, null, episodioCv)
    }

    override fun listOneEpisodio(nomeEpisodio: String , ): Episodio {
        val episodioCursor = episodiosDb.query(
            true,
            TABELA_EPISODIO,
            null, //tabela
            "${COLUNA_NOME_SERIE} = ? AND $COLUNA_TEMPORADA = ?", //where,
            null,
            null,
            null,
            null,
            null
        )

        return if(episodioCursor.moveToFirst()){
            with(episodioCursor){
                Episodio(
                    getString(getColumnIndexOrThrow(COLUNA_NUMERO_EPISODIO)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME_EPISODIO)),
                    getString(getColumnIndexOrThrow(COLUNA_DURACAO)),
                    getString(getColumnIndexOrThrow(COLUNA_ASSISTIDO)),
                    getString(getColumnIndexOrThrow(COLUNA_NOME_SERIE)),
                    getString(getColumnIndexOrThrow(COLUNA_TEMPORADA))
                )
            }
        }
        else{
            Episodio()
        }
    }

    override fun listAllEpisodio(nomeSerie: String , temporada: String): MutableList<Episodio> {
        val episodiosCursor = episodiosDb.query(
            true,
            TABELA_EPISODIO,
            null, //tabela
            "${COLUNA_NOME_SERIE} = ? AND $COLUNA_TEMPORADA = ?", //where,
            arrayOf(nomeSerie, temporada), //valores do where
            null,
            null,
            null,
            null
        )



        val episodiosList: MutableList<Episodio> = mutableListOf()
        while(episodiosCursor.moveToNext()){
            with(episodiosCursor){
                episodiosList.add(
                    Episodio(
                        getString(getColumnIndexOrThrow(COLUNA_NUMERO_EPISODIO)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME_EPISODIO)),
                        getString(getColumnIndexOrThrow(COLUNA_DURACAO)),
                        getString(getColumnIndexOrThrow(COLUNA_ASSISTIDO)),
                        getString(getColumnIndexOrThrow(COLUNA_NOME_SERIE)),
                        getString(getColumnIndexOrThrow(COLUNA_TEMPORADA))
                    )
                )
            }
        }
        return episodiosList
    }

    override fun updateEpisodio(episodio: Episodio): Int {
        val episodioCv = convertEpisodioContentValues(episodio)
        return episodiosDb.update(TABELA_EPISODIO, episodioCv,
            "${COLUNA_NUMERO_EPISODIO} = ? AND ${COLUNA_NOME_SERIE} = ? AND ${COLUNA_TEMPORADA} = ?",
            arrayOf(episodio.numeroEpisodio, episodio.nomeSerie, episodio.temporada))
    }

    override fun deleteEpisodio(episodio: Episodio): Int {
        return episodiosDb.delete(TABELA_EPISODIO,
            "${COLUNA_NUMERO_EPISODIO} = ? AND ${COLUNA_NOME_SERIE} = ? AND ${COLUNA_TEMPORADA} = ?",
            arrayOf(episodio.numeroEpisodio, episodio.nomeSerie, episodio.temporada))
    }

    private fun convertEpisodioContentValues(episodio: Episodio): ContentValues = ContentValues().also{
        with(it){
            put(COLUNA_NUMERO_EPISODIO, episodio.numeroEpisodio)
            put(COLUNA_NOME_EPISODIO, episodio.nomeEpisodio)
            put(COLUNA_DURACAO, episodio.duracao)
            put(COLUNA_ASSISTIDO, episodio.assistido)
            put(COLUNA_NOME_SERIE, episodio.nomeSerie)
            put(COLUNA_TEMPORADA, episodio.temporada)
        }
    }
}