package com.example.seriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.seriesmanager.adapter.episodio.EpisodioRvAdapter
import com.example.seriesmanager.controller.episodio.EpisodioController
import com.example.seriesmanager.databinding.ActivityMainEpisodioBinding
import com.example.seriesmanager.model.episodio.Episodio
import com.example.seriesmanager.model.serie.Serie
import com.example.seriesmanager.model.temporada.Temporada

class MainEpisodioActivity : AppCompatActivity(), OnEpisodioClickListener {

    companion object Extras{
        const val EXTRA_SERIE = "EXTRA_SERIE"
        const val EXTRA_TEMPORADA = "EXTRA_TEMPORADA"
        const val EXTRA_EPISODIO = "EXTRA_EPISODIO"
        const val EXTRA_POSICAO = "EXTRA_POSICAO"
        const val EXTRA_NUMERO_EP = "EXTRA_NUMERO_EP"
    }

    private val activityMainEpisodioBinding: ActivityMainEpisodioBinding by lazy {
        ActivityMainEpisodioBinding.inflate(layoutInflater)
    }

    private lateinit var serie: Serie
    private lateinit var temporada: Temporada
    private lateinit var episodioActivityResultLauncher: ActivityResultLauncher<Intent>
    private lateinit var editarEpisodioActivityResultLauncher: ActivityResultLauncher<Intent>


    //controller
    private val episodioController: EpisodioController by lazy{
        EpisodioController(this)
    }

    //data source
    private val episodioslist: MutableList<Episodio> by lazy {
        serie = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_SERIE)!!
        temporada = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_TEMPORADA)!!
        episodioController.listAllEpisodio(serie.nome, temporada.numeroTemporada)
    }


    //adapter
    private val episodioAdapter: EpisodioRvAdapter by lazy{
        EpisodioRvAdapter(this, episodioslist)
    }

    //layoutManager
    private val episodiosLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainEpisodioBinding.root)

        setTitle("Episodios")

        //associar adapter e layoutManager ao recyclerView
        activityMainEpisodioBinding.episodiosRv.adapter = episodioAdapter
        activityMainEpisodioBinding.episodiosRv.layoutManager = episodiosLayoutManager

        episodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Episodio>(MainEpisodioActivity.EXTRA_EPISODIO)?.apply {
                    if  (episodioController.insertEpisodio(this).compareTo(-1) != 0) {
                        episodioslist.add(this)
                        episodioslist.sortBy { it.numeroEpisodio }
                        episodioAdapter.notifyDataSetChanged()
                        Toast.makeText(applicationContext, "Episodio cadastrado com sucesso!", Toast.LENGTH_SHORT).show()
                    }
                    else{
                        Toast.makeText(applicationContext, "Falha ao cadastrar episodio!", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }

        editarEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(MainEpisodioActivity.EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Episodio>(MainEpisodioActivity.EXTRA_EPISODIO)?.apply {
                    if(posicao != null && posicao != -1) {
                        episodioController.updateEpisodio(this)
                        episodioslist[posicao] = this
                        episodioAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainEpisodioBinding.adicionarEpisodioFab.setOnClickListener{
            serie = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_SERIE)!!
            temporada = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_TEMPORADA)!!

            episodioActivityResultLauncher.launch(Intent(this,
                EpisodioActivity::class.java).putExtra(MainTemporadaActivity.EXTRA_SERIE, serie).
                putExtra(MainTemporadaActivity.EXTRA_TEMPORADA, temporada).putExtra(
                EXTRA_NUMERO_EP, episodioslist.size))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = episodioAdapter.posicao

        return when (item.itemId){
            R.id.detalharEpisodioMi -> {
                //ver detalhes serie
                val episodio = episodioslist[posicao]
                val consultarEpisodioIntent = Intent(this, EpisodioActivity::class.java)
                consultarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio)
                startActivity(consultarEpisodioIntent)

                true
            }
            R.id.editarEpisodioMi -> {
                //editar serie
                val episodio = episodioslist[posicao]
                val editarEpisodioIntent = Intent(this, EpisodioActivity::class.java)

                editarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio)
                editarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_POSICAO, posicao)
                editarEpisodioActivityResultLauncher.launch(editarEpisodioIntent)

                true
            }
            R.id.removerEpisodioMi -> {
                //remover serie
                episodioController.deleteEpisodio(episodioslist[posicao])
                episodioslist.removeAt(posicao)
                episodioAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }

        return super.onContextItemSelected(item)
    }

    override fun onEpisodioClick(posicao: Int) {
        val episodio = episodioslist[posicao]
        val consultarEpisodioIntent = Intent(this, EpisodioActivity::class.java)
        consultarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio)
        startActivity(consultarEpisodioIntent)
    }
}