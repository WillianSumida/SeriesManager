package com.example.seriesmanager.adapter.serie

import android.view.*
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.OnSerieClickListener
import com.example.seriesmanager.R
import com.example.seriesmanager.databinding.LayoutSerieBinding
import com.example.seriesmanager.model.serie.Serie

class SerieRvAdapter (
    private val eventClickSerie: OnSerieClickListener,
    private val seriesList: MutableList<Serie>
): RecyclerView.Adapter<SerieRvAdapter.SerieLayoutHolder>(){

    //posicao que sera recuperado pelo menu de contexto
    var posicao: Int = -1

    //view holder
    inner class SerieLayoutHolder(layoutSerieBinding: LayoutSerieBinding):
        RecyclerView.ViewHolder(layoutSerieBinding.root), View.OnCreateContextMenuListener{
            val nomeTv: TextView = layoutSerieBinding.nomeTv
            val emissoraTv: TextView = layoutSerieBinding.emissoraTv
            val generoTv: TextView =  layoutSerieBinding.generoTv

            init{
                itemView.setOnCreateContextMenuListener(this)
            }

            override fun onCreateContextMenu(
                menu: ContextMenu?,
                view: View?,
                menuInfo: ContextMenu.ContextMenuInfo?
            ) {
                MenuInflater(view?.context).inflate(R.menu.context_menu_serie, menu)
            }
    }

    //quando uma nova celular precisa ser criada
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SerieLayoutHolder {
        //criar celula
        val layoutSerieBinding = LayoutSerieBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        //criar viewHolder associado a nova celula
        return SerieLayoutHolder(layoutSerieBinding)
    }

    //quando precisar atualizar valores da celula
    override fun onBindViewHolder(holder: SerieLayoutHolder, position: Int) {
        //busca serie
        val serie = seriesList[position]

        //atualizar valor do viewHolder
        with(holder) {
            nomeTv.text = serie.nome
            emissoraTv.text = serie.emissora
            generoTv.text = serie.genero
            itemView.setOnClickListener {
                eventClickSerie.onSerieClick(position)
            }
            itemView.setOnLongClickListener{
                posicao = position
                false
            }
        }
    }

    //retorna a quantidade de itens
    override fun getItemCount(): Int= seriesList.size
}