package com.example.seriesmanager.adapter.temporada

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.OnTemporadaClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutTemporadaBinding
import com.example.seriesmanager.model.temporada.Temporada

class TemporadaRvAdapter (
    val eventClickTemporada: OnTemporadaClickListener,
    private val temporadasList: MutableList<Temporada>
): RecyclerView.Adapter<TemporadaRvAdapter.TemporadaLayoutHolder>(){

    //posicao que sera recuperado pelo menu de contexto
    var posicao: Int = -1

    //view holder
    inner class TemporadaLayoutHolder(layoutTemporadaBinding: LayoutTemporadaBinding):
        RecyclerView.ViewHolder(layoutTemporadaBinding.root), View.OnCreateContextMenuListener{
        val numeroTemporadaTv: TextView = layoutTemporadaBinding.numeroTemporadaTv
        val anoLancamentoTv: TextView = layoutTemporadaBinding.anoLancamentoTv
        val qtdEpisodesTv: TextView =  layoutTemporadaBinding.qtdeEpisodesTv
        val nomeSerieTv: TextView = layoutTemporadaBinding.nomeSerieTv

        init{
            itemView.setOnCreateContextMenuListener(this)
        }

        override fun onCreateContextMenu(
            menu: ContextMenu?,
            view: View?,
            menuInfo: ContextMenu.ContextMenuInfo?
        ) {
            MenuInflater(view?.context).inflate(R.menu.context_menu_temporada, menu)
        }
    }

    //quando uma nova celula precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TemporadaRvAdapter.TemporadaLayoutHolder {
        //criar celula
        val layoutTemporadaBinding = LayoutTemporadaBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //criar viewHolder associado a nova celula
        return TemporadaLayoutHolder(layoutTemporadaBinding)
    }

    //quando precisar atualizar valores da celula
    override fun onBindViewHolder(holder: TemporadaRvAdapter.TemporadaLayoutHolder, position: Int) {
        //busca serie
        val temporada = temporadasList[position]

        //atualizar valor do viewHolder
        with(holder) {
            numeroTemporadaTv.text = temporada.numeroTemporada
            anoLancamentoTv.text = temporada.anoDeLancamento
            qtdEpisodesTv.text = temporada.qtdEpisodes
            nomeSerieTv.text = temporada.nomeSerie

            itemView.setOnClickListener {
                eventClickTemporada.onTemporadaClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    //retorna a quantidade de itens
    override fun getItemCount(): Int= temporadasList.size
}