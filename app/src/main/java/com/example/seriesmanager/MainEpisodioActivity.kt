package com.example.seriesmanager

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
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
    private val episodiosist: MutableList<Episodio> by lazy {
        serie = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_SERIE)!!
        temporada = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_TEMPORADA)!!
        episodioController.listAllEpisodio(serie.nome, temporada.numeroTemporada)
    }

    //adapter
    private val episodioAdapter: EpisodioRvAdapter by lazy{
        EpisodioRvAdapter(this, episodiosist)
    }

    //layoutManager
    private val episodiosLayoutManager: LinearLayoutManager by lazy{
        LinearLayoutManager(this)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(activityMainEpisodioBinding.root)

        //associar adapter e layoutManager ao recyclerView
        activityMainEpisodioBinding.episodiosRv.adapter = episodioAdapter
        activityMainEpisodioBinding.episodiosRv.layoutManager = episodiosLayoutManager


        episodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                resultado.data?.getParcelableExtra<Episodio>(MainEpisodioActivity.EXTRA_EPISODIO)?.apply {
                    episodioController.insertEpisodio(this)
                    episodiosist.add(this)
                    episodioAdapter.notifyDataSetChanged()
                }
            }
        }

        editarEpisodioActivityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
                resultado->

            if (resultado.resultCode == RESULT_OK) {
                val posicao = resultado.data?.getIntExtra(MainEpisodioActivity.EXTRA_POSICAO, -1)
                resultado.data?.getParcelableExtra<Temporada>(MainEpisodioActivity.EXTRA_EPISODIO)?.apply {
                    if(posicao != null && posicao != -1) {
                        episodioController.updateEpisodio(this)
                        episodiosist[posicao] = this
                        episodioAdapter.notifyDataSetChanged()
                    }
                }
            }
        }

        activityMainEpisodioBinding.adicionarEpisodioFab.setOnClickListener{
            serie = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_SERIE)!!
            temporada = intent.getParcelableExtra(MainTemporadaActivity.EXTRA_TEMPORADA)!!

            episodioActivityResultLauncher.launch(Intent(this,
                TemporadaActivity::class.java).putExtra(MainTemporadaActivity.EXTRA_SERIE, serie).putExtra(MainTemporadaActivity.EXTRA_TEMPORADA, temporada))
        }
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        val posicao = episodioAdapter.posicao

        return when (item.itemId){
            R.id.detalharEpisodioMi -> {
                //ver detalhes serie
                val episodio = episodiosist[posicao]
                val consultarEpisodioIntent = Intent(this, EpisodioActvitiy::class.java)
                consultarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio)
                startActivity(consultarEpisodioIntent)

                true
            }
            R.id.editarEpisodioMi -> {
                //editar serie
                val episodio = episodiosist[posicao]
                val editarEpisodioIntent = Intent(this, EpisodioActivity::class.java)

                editarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_EPISODIO, episodio)
                editarEpisodioIntent.putExtra(MainEpisodioActivity.EXTRA_POSICAO, posicao)
                editarEpisodioActivityResultLauncher.launch(editarEpisodioIntent)

                true
            }
            R.id.removerTemporadaMi -> {
                //remover serie
                episodioController.deleteEpisodio(episodiosist[posicao])
                episodiosist.removeAt(posicao)
                episodioAdapter.notifyDataSetChanged()
                true
            }
            else -> { false }
        }

        return super.onContextItemSelected(item)
    }

    override fun onEpisodioClick(posicao: Int) {
        TODO("Not yet implemented")
    }
}