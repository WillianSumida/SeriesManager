package com.example.seriesmanager.adapter.temporada

import androidx.recyclerview.widget.RecyclerView
import com.example.seriesmanager.OnTemporadaClickListener
import com.example.seriesmanager.model.temporada.Temporada

class TemporadaRvAdapter(
    private val eventClickTemporada: OnTemporadaClickListener,
    private val temporadasList: MutableList<Temporada>
): RecyclerView.Adapter<TemporadaRvAdapter.TemporadaLayoutHolder>(){

}