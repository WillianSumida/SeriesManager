package com.example.seriesmanager.model.firebase

import com.example.seriesmanager.model.SerieDao
import com.example.seriesmanager.model.serie.Serie
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase

class SerieFirebase: SerieDao {

    companion object {
        private val BD_SERIE = "serie"
    }

    //referencia para o realtime database -> series
    private val seriesRtDb = Firebase.database.getReference(BD_SERIE)

    //lista de series que simula consulta
    private val seriesList: MutableList<Serie> = mutableListOf()
    init{
        seriesRtDb.addChildEventListener(object: ChildEventListener{
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val novaSerie: Serie? = snapshot.value as? Serie
                novaSerie?.apply {
                    if(seriesList.find { it.nome == this.nome} == null){
                        seriesList.add(novaSerie)
                    }
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {
                val serieAlterada: Serie? = snapshot.value as? Serie
                serieAlterada?.apply {
                    seriesList[seriesList.indexOfFirst { it.nome == this.nome }] = this
                }
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                val serieDeletada: Serie? = snapshot.value as? Serie
                serieDeletada?.apply {
                    seriesList.remove(this)
                }
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                //nao usa
            }

            override fun onCancelled(error: DatabaseError) {
                //nao usa
            }
        })
        seriesRtDb.addListenerForSingleValueEvent(object: ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                seriesList.clear()
                snapshot.children.forEach{
                    val serie: Serie = it.getValue<Serie>()?: Serie()
                    seriesList.add(serie)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                //nao usa
            }
        })
    }


    override fun createSerie(serie: Serie): Long {
        createOrUpdate(serie)
        return 0L
    }

    override fun listOneSerie(nome: String): Serie {
        return seriesList.firstOrNull { it.nome == nome } ?: Serie()
    }

    override fun litsAllSerie(): MutableList<Serie> = seriesList

    override fun updateSerie(serie: Serie): Int {
        createOrUpdate(serie)
        return 1
    }

    override fun deleteSerie(nome: String): Int {
        seriesRtDb.child(nome).removeValue()
        return 1
    }

    private fun createOrUpdate (serie: Serie){
        seriesRtDb.child(serie.nome).setValue(serie)
    }
}