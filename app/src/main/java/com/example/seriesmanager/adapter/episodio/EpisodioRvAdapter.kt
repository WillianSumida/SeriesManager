package com.example.seriesmanager.adapter.episodio

import android.util.Log
import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.OnEpisodioClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.adapter.temporada.TemporadaRvAdapter
import com.example.seriesmanager.databinding.LayoutEpisodioBinding
import com.example.seriesmanager.databinding.LayoutTemporadaBinding
import com.example.seriesmanager.model.episodio.Episodio
import com.example.seriesmanager.model.temporada.Temporada

class EpisodioRvAdapter(
    val eventClickEpisodio: OnEpisodioClickListener,
    private val episodiosList: MutableList<Episodio>
): RecyclerView.Adapter<EpisodioRvAdapter.EpisodioLayoutHolder>(){
    //posicao que sera recuperado pelo menu de contexto
    var posicao: Int = -1

    //view holder
    inner class EpisodioLayoutHolder(layoutEpisodioBinding: LayoutEpisodioBinding):
        RecyclerView.ViewHolder(layoutEpisodioBinding.root), View.OnCreateContextMenuListener{
        val numeroEpisodioTv: TextView = layoutEpisodioBinding.numeroEpisodioTv
        val nomeEpisodioTv: TextView = layoutEpisodioBinding.nomeEpisodioTv
        val duracaoTv: TextView =  layoutEpisodioBinding.duracaoTv
        val nomeSerieTv: TextView = layoutEpisodioBinding.nomeSerieTv

        init{
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_episodio, menu)
        }
    }

    //quando uma nova celula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodioRvAdapter.EpisodioLayoutHolder {
        //criar celula
        val layoutEpisodioBinding = LayoutEpisodioBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //criar viewHolder associado a nova celula
        return EpisodioLayoutHolder(layoutEpisodioBinding)
    }

    //quando precisar atualizar valores da celula
    override fun onBindViewHolder(holder: EpisodioRvAdapter.EpisodioLayoutHolder, position: Int) {
        //busca serie
        val episodio = episodiosList[position]

        //atualizar valor do viewHolder
        with(holder) {
            numeroEpisodioTv.text = episodio.numeroEpisodio
            nomeEpisodioTv.text = episodio.nomeEpisodio
            duracaoTv.text = episodio.duracao
            nomeSerieTv.text = episodio.nomeSerie

            itemView.setOnClickListener {
                eventClickEpisodio.onEpisodioClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    //retorna a quantidade de itens
    override fun getItemCount(): Int= episodiosList.size
}
