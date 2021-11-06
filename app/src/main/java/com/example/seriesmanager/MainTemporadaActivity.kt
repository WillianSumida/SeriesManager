package com.example.seriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.adapter.serie.SerieRvAdapter
import com.example.seriesmanager.adapter.temporada.TemporadaRvAdapter
import com.example.seriesmanager.controller.serie.SerieController
import com.example.seriesmanager.controller.temporada.TemporadaController
import com.example.seriesmanager.databinding.ActivityMainBinding
import com.example.seriesmanager.databinding.ActivityMainTemporadaBinding
import com.example.seriesmanager.databinding.ActivityTemporadaBinding
import com.example.seriesmanager.model.serie.Serie
import com.example.seriesmanager.model.temporada.Temporada

class MainTemporadaActivity : AppCompatActivity(), OnTemporadaClickListener {

    companion object Extras{
        const val EXTRA_SERIE = "EXTRA_SERIE"
        const val EXTRA_TEMPORADA = "EXTRA_TEMPORADA"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
    }

    private val activityMainTemporadaBinding: ActivityMainTemporadaBinding by lazy {
        ActivityMainTemporadaBinding.inflate(layoutInflater)
    }

    private lateinit var serie: Serie
    private lateinit var temporadaActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarTemporadaActivityResultLauncher: ActivityResultLauncher<Intent>

    //controller
    private val temporadaController: TemporadaController by lazy{
        TemporadaController(this)
    }

    //data source
    private val temporadasList: MutableList<Temporada> by lazy {
        serie = intent.getParcelableExtra(MainActivity.EXTRA_SERIE)!!
        temporadaController.listAllTemporada(serie.nome.toString())
    }

    //adapter
    private val temporadaAdapter: TemporadaRvAdapter by lazy{
        TemporadaRvAdapter(this, temporadasList)
    }

    //layoutManager
    private val temporadasLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainTemporadaBinding.root)

        //associar adapter e layoutManager ao recyclerView
        activityMainTemporadaBinding.temporadasRv.adapter = temporadaAdapter
        activityMainTemporadaBinding.temporadasRv.layoutManager = temporadasLayoutManager


        temporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Temporada>(MainTemporadaActivity.EXTRA_TEMPORADA)?.apply {
                    temporadaController.insertTemporada(this)
                    temporadasList.add(this)
                    temporadaAdapter.notifyDataSetChanged()
                }
            }
        }

        editarTemporadaActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(MainTemporadaActivity.EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Temporada>(MainTemporadaActivity.EXTRA_TEMPORADA)?.apply {
                    if(posicao != null && posicao != -1) {
                        temporadaController.updateTemporada(this)
                        temporadasList[posicao] = this
                        temporadaAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainTemporadaBinding.adicionarTemporadaFab.setOnClickListener{
            temporadaActivityResultLauncher.launch(Intent(this, TemporadaActivity::class.java))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = temporadaAdapter.posicao

        return when (item.itemId){
            R.id.detalharTemporadaMi -> {
                //ver detalhes serie
                val temporada = temporadasList[posicao]
                val consultarTemporadaIntent = Intent(this, TemporadaActivity::class.java)
                consultarTemporadaIntent.putExtra(MainTemporadaActivity.EXTRA_TEMPORADA, temporada)
                startActivity(consultarTemporadaIntent)

                true
            }
            R.id.editarTemporadaMi -> {
                //editar serie
                val temporada = temporadasList[posicao]
                val editarTemporadaIntent = Intent(this, TemporadaActivity::class.java)

                editarTemporadaIntent.putExtra(MainTemporadaActivity.EXTRA_TEMPORADA, temporada)
                editarTemporadaIntent.putExtra(MainTemporadaActivity.EXTRA_POSICAO, posicao)
                editarTemporadaActivityResultLauncher.launch(editarTemporadaIntent)

                true
            }
            R.id.removerSerieMi -> {
                //remover serie
                temporadaController.deleteTemporada(temporadasList[posicao].numeroTemporada)
                temporadasList.removeAt(posicao)
                temporadaAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }

        return super.onContextItemSelected(item)
    }

    override fun onTemporadaClick(posicao: Int) {
        //mandar para a pag de capitulo
    }
}