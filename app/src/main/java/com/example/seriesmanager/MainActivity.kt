package com.example.seriesmanager

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.adapter.serie.SerieRvAdapter
import com.example.seriesmanager.controller.serie.SerieController
import com.example.seriesmanager.databinding.ActivityMainBinding
import com.example.seriesmanager.model.serie.Serie
import com.example.seriesmanager.model.temporada.Temporada

class MainActivity : AppCompatActivity(), OnSerieClickListener {

    companion object Extras{
        const val EXTRA_SERIE = "EXTRA_SERIE"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainBinding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private lateinit var serieActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarSerieActivityResultLauncher: ActivityResultLauncher<Intent>

    //controller
    private val serieController: SerieController by lazy{
        SerieController(this)
    }

    //data source
    private val seriesList: MutableList<Serie> by lazy {
        serieController.listAllSerie()
    }

    //adapter
    private val serieAdapter: SerieRvAdapter by lazy{
        SerieRvAdapter(this, seriesList)
    }

    //layoutManager
    private val seriesLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainBinding.root)

        setTitle("Series")

        //associar adapter e layoutManager ao recyclerView
        activityMainBinding.seriesRv.adapter = serieAdapter
        activityMainBinding.seriesRv.layoutManager = seriesLayoutManager

        serieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    if  (serieController.insertSerie(this).compareTo(-1) != 0) {
                        seriesList.add(this)
                        serieAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Serie cadastrada com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Falha ao cadastrar serie!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        editarSerieActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Serie>(EXTRA_SERIE)?.apply {
                    if(posicao != null && posicao != -1) {
                        serieController.updateSerie(this)
                        seriesList[posicao] = this
                        serieAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainBinding.adicionarSerieFab.setOnClickListener{
            serieActivityResultLauncher.launch(Intent(this, SerieActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = serieAdapter.posicao

        return when (item.itemId){
            R.id.detalharSerieMi -> {
                //ver detalhes serie
                val serie = seriesList[posicao]
                val consultarSerieIntent = Intent(this, SerieActivity::class.java)
                consultarSerieIntent.putExtra(EXTRA_SERIE, serie)
                startActivity(consultarSerieIntent)

                true
            }
            R.id.editarSerieMi -> {
                //editar serie
                val serie = seriesList[posicao]
                val editarSerieIntent = Intent(this, SerieActivity::class.java)

                editarSerieIntent.putExtra(EXTRA_SERIE, serie)
                editarSerieIntent.putExtra(EXTRA_POSICAO, posicao)
                editarSerieActivityResultLauncher.launch(editarSerieIntent)

                true
            }
            R.id.removerSerieMi -> {
                //remover serie
                serieController.deleteSerie(seriesList[posicao].nome)
                seriesList.removeAt(posicao)
                serieAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }

        return super.onContextItemSelected(item)
    }

    override fun onSerieClick(posicao: Int) {
        //levar para a pagina de series
        val serie = seriesList[posicao]
        val consultarSerieTemporadaIntent = Intent(this, MainTemporadaActivity::class.java)
        consultarSerieTemporadaIntent.putExtra(EXTRA_SERIE, serie)
        startActivity(consultarSerieTemporadaIntent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.atualizarMi -> {
            serieAdapter.notifyDataSetChanged()
            true
        }
        else -> {false}
    }
}