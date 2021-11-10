package com.example.seriesmanager.model

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.util.Log
import com.example.seriesmanager.R
import com.example.seriesmanager.model.serie.Serie

class SerieSqlite(contexto: Context): SerieDao {

    companion object {
        private val BD_SERIES_MANAGER = "seriesManager"
        private val TABELA_SERIE = "serie"
        private val COLUNA_NOME = "nome"
        private val COLUNA_ANO_LANCAMENTO = "anoLancamento"
        private val COLUNA_EMISSORA = "emissora"
        private val COLUNA_GENERO = "genero"


        private val CRIAR_TABELA_SERIE_STMT = "CREATE TABLE IF NOT EXISTS $TABELA_SERIE (" +
                "$COLUNA_NOME TEXT NOT NULL, " +
                "$COLUNA_ANO_LANCAMENTO TEXT NOT NULL, " +
                "$COLUNA_EMISSORA TEXT NOT NULL, " +
                "$COLUNA_GENERO TEXT NOT NULL, " +
                "PRIMARY KEY (${COLUNA_NOME}))"
    }

    //Referecia para o db
    private val seriesDb: SQLiteDatabase

    init {
        seriesDb = contexto.openOrCreateDatabase(BD_SERIES_MANAGER, Context.MODE_PRIVATE, null)
        seriesDb.setForeignKeyConstraintsEnabled(true)
        try {
            seriesDb.execSQL(CRIAR_TABELA_SERIE_STMT)
        } catch (se: android.database.SQLException) {
            Log.e(contexto.getString(R.string.app_name), se.toString())
        }
    }

    override fun createSerie(serie: Serie): Long {
        val serieCv = convertSerieContentValues(serie)
        return seriesDb.insert(TABELA_SERIE, null, serieCv)
    }

    override fun listOneSerie(nome: String): Serie {
        val serieCursor = seriesDb.query(
            true,
            TABELA_SERIE,
            null, //tabela
            "$COLUNA_NOME = ?", //where,
            arrayOf(nome), //valores do where
            null,
            null,
            null,
            null
        )

        return if (serieCursor.moveToFirst()) {
            with(serieCursor) {
                Serie(
                    getString(getColumnIndexOrThrow(COLUNA_NOME)),
                    getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                    getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                    getString(getColumnIndexOrThrow(COLUNA_GENERO)),
                )
            }
        } else {
            Serie()
        }
    }

    override fun litsAllSerie(): MutableList<Serie> {
        val serieCursor = seriesDb.query(
            true,
            TABELA_SERIE,
            null, //tabela
            null, //where,
            arrayOf(), //valores do where
            null,
            null,
            null,
            null
        )

        val seriesList: MutableList<Serie> = mutableListOf()
        while (serieCursor.moveToNext()) {
            with(serieCursor) {
                seriesList.add(
                    Serie(
                        getString(getColumnIndexOrThrow(COLUNA_NOME)),
                        getString(getColumnIndexOrThrow(COLUNA_ANO_LANCAMENTO)),
                        getString(getColumnIndexOrThrow(COLUNA_EMISSORA)),
                        getString(getColumnIndexOrThrow(COLUNA_GENERO)),
                    )
                )
            }
        }
        return seriesList
    }

    override fun updateSerie(serie: Serie): Int {
        val serieCv = convertSerieContentValues(serie)
        return seriesDb.update(TABELA_SERIE, serieCv, "$COLUNA_NOME = ?", arrayOf(serie.nome))
    }

    override fun deleteSerie(nome: String): Int {
        return seriesDb.delete(TABELA_SERIE, "$COLUNA_NOME = ?", arrayOf(nome))
    }

    private fun convertSerieContentValues(serie: Serie): ContentValues = ContentValues().also {
        with(it) {
            put(COLUNA_NOME, serie.nome)
            put(COLUNA_ANO_LANCAMENTO, serie.anoDeLancamento)
            put(COLUNA_EMISSORA, serie.emissora)
            put(COLUNA_GENERO, serie.genero)
        }
    }
}