package com.example.seriesmanager.model.firebase

import com.example.seriesmanager.model.episodio.Episodio
import com.example.seriesmanager.model.episodio.EpisodioDao
import com.example.seriesmanager.model.temporada.Temporada
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class EpisodioFirebase: EpisodioDao {

    companion object {
        private val BD_SERIE = "serie"
    }

    //referencia para o realtime database -> episodios
    private val episodiosRtDb = Firebase.database.getReference(BD_SERIE)

    //lista de episodios que simula consulta
    private val episodiosList: MutableList<Episodio> = mutableListOf()
    init{
        episodiosRtDb.addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novoEpisodio: Episodio? = snapshot.child("serie").child("temporada").child("episodio").value as? Episodio
                novoEpisodio?.apply {
                    if(episodiosList.find { it.numeroEpisodio == this.numeroEpisodio} == null){
                        episodiosList.add(novoEpisodio)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val episodioAlterado: Episodio? = snapshot.child("serie").child("temporada").child("episodio").value as? Episodio
                episodioAlterado?.apply {
                    episodiosList[episodiosList.indexOfFirst { it.numeroEpisodio == this.numeroEpisodio }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val episodioDeletado: Episodio? = snapshot.child("serie").child("temporada").child("episodio").value as? Episodio
                episodioDeletado?.apply {
                    episodiosList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //nao usa
            }

            override fun onCancelled(error: DatabaseError) {
                //nao usa
            }
        })
    }


    override fun createEpisodio(episodio: Episodio): Long {
        createOrUpdate(episodio)
        return 0L
    }

    override fun listOneEpisodio(episodio: Episodio): Episodio {
        return episodiosList.firstOrNull { it == episodio } ?: Episodio()
    }

    override fun listAllEpisodio(nomeSerie: String, temporada: String): MutableList<Episodio> {
        episodiosRtDb.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                episodiosList.clear()
                snapshot.child(nomeSerie).
                child("temporada").
                child(temporada).
                child("episodio").
                children.forEach{
                    val episodio: Episodio = it.getValue<Episodio>()?: Episodio()
                    episodiosList.add(episodio)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //nao usa
            }
        })

        return episodiosList
    }

    override fun updateEpisodio(episodio: Episodio): Int {
        createOrUpdate(episodio)
        return 1
    }

    override fun deleteEpisodio(episodio: Episodio): Int {
        episodiosRtDb.child(episodio.nomeSerie).
        child("temporada").
        child(episodio.temporadaNumero).
        child("episodio").
        child(episodio.numeroEpisodio).removeValue()
        return 1
    }

    private fun createOrUpdate (episodio: Episodio){
        episodiosRtDb.child(episodio.nomeSerie).
        child("temporada").
        child(episodio.temporadaNumero).
        child("episodio").
        child(episodio.numeroEpisodio).
        setValue(episodio)
    }
}